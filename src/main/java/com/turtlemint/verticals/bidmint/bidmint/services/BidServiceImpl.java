package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.*;
import com.turtlemint.verticals.bidmint.bidmint.dto.BidDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.BidStats;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBidService;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import com.turtlemint.verticals.bidmint.bidmint.utils.NotificationServiceProvider;
import com.turtlemint.verticals.bidmint.bidmint.utils.ScoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.*;

@Service("bidService")
public class BidServiceImpl implements IBidService {

    @Autowired
    BidMintDaoFactory bidMintDaoFactory;

    @Autowired
    IProposalService proposalService;

    @Autowired
    NotificationServiceProvider notificationServiceProvider;

    @Override
    public Mono<BuyerDTO> createBid(Bid bid) {
        BuyerDTO buyerDTO = new BuyerDTO();
        if (Objects.isNull(bid.getId())) {
            UUID uuid = UUID.randomUUID();
            String proposalId = uuid.toString();
            bid.setId(proposalId);
            bid.setCreatedAt(Instant.now().getEpochSecond());
        } else {
            bid.setUpdatedAt(Instant.now().getEpochSecond());
        }
        bid.setStatus(BidMintEnums.DRAFT);
        return bidMintDaoFactory.getBidDao().createBidDao(bid).flatMap(
                bidNew -> {
                    Map<String, Object> bidMap = new HashMap<>();
                    bidMap.put("bidId", bidNew.getId());
                    buyerDTO.setMeta(bidMap);
                    buyerDTO.setStatusCode(HttpStatus.CREATED.value());
                    buyerDTO.setMessage("Bid Created");
                    return Mono.just(buyerDTO);
                }
        ).onErrorResume(error -> {
            buyerDTO.setMessage("Bid Not Created");
            buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return Mono.justOrEmpty(buyerDTO);
        });
    }

    @Override
    public Mono<BuyerDTO> publishBid(String bidId, Double amount, Double percent) {
        BuyerDTO buyerDTO = new BuyerDTO();
        Update update = new Update();
        update.set(PUBLISHED_AT, Instant.now().getEpochSecond());
        update.set(AMOUNT, amount);
        Bid bid = bidMintDaoFactory.getBidDao().findById(bidId);
        update.set("percent", percent);
        bid.setStatus(BidMintEnums.ACTIVE);
        update.set(STATUS, BidMintEnums.ACTIVE);
        if (percent < 100) {
            update.set(STATUS, BidMintEnums.PARTIAL);
            bid.setStatus(BidMintEnums.PARTIAL);
        }
        Proposal proposal = bidMintDaoFactory.getProposalDao().findById(bid.getProposalId());
        bid.setAmount(amount);
        bid.setPublishedAt(Instant.now().getEpochSecond());
        Integer agreed = ScoreUtils.computeAgreementOnQuestions(proposal.getProposalQuestions().get(0),
                bid.getProposalAnswers().get(0));
        bid.setAgreementOnQuestions(agreed);
        update.set("agreementOnQuestions", agreed);
        if (proposal.getNumberOfParticipants() == 0) {
            BidStats bidStats = new BidStats();
            bidStats.setBidScore(100.00);
            bidStats.setExcessAmount(0.00);
            bidStats.setAgreementOnQuestions(agreed);
            //This is proposal best bid
            bid.setBidStats(bidStats);
            proposal.setBestBid(bid);
            proposal.setAvgAgreementOnQuestions(bid.getAgreementOnQuestions());
            proposal.setAvgBidAmount(bid.getAmount());
        } else {
            getBestBidAndUpdateOtherBids(proposal, bid);
        }
        update.set("bidStats", bid.getBidStats());
        return bidMintDaoFactory.getBidDao().updateBidById(bidId, update).flatMap(
                updateResult -> {
                    if (notificationServiceProvider.sendNotification(getNsTemplateForBuyer(bid), "Bid")
                            && proposalService.updateProposalDetails(bid.getProposalId(), proposal)) {
                        buyerDTO.setStatusCode(HttpStatus.OK.value());
                        buyerDTO.setMessage("Bid is Published and Notification is Triggered");
                        return Mono.just(buyerDTO);
                    }
                    buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    buyerDTO.setMessage("Bid is Published but notification failed");
                    return Mono.just(buyerDTO);
                }
        ).onErrorResume(error -> {
            buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            buyerDTO.setMessage("Bid is not Published");
            return Mono.just(buyerDTO);
        });
    }

    public void getBestBidAndUpdateOtherBids(Proposal proposal, Bid bid) {
        Bid bestBid = proposal.getBestBid();
        double score = ScoreUtils.calculateBidScoreWRTBestBid(bid, bestBid);
        if (score > bestBid.getBidStats().getBidScore()) {
            // update proposal
            proposal.setBestBid(bid);
            proposal.setAvgBidAmount(proposal.getAvgBidAmount() + bid.getAmount() / 2);
            proposal.setAvgAgreementOnQuestions(proposal.getAvgAgreementOnQuestions() + bid.getAgreementOnQuestions() / 2);
        }
        reCalculateOtherBidsScore(bid, proposal);

    }

    public void reCalculateOtherBidsScore(Bid currentBid, Proposal proposal) {
        List<Bid> allBids = bidMintDaoFactory.getBidDao().getAllBidsByProposalId(proposal.getId());
        List<Double> bidScores = new ArrayList<>();
        // check for same score/input check
        for (Bid bid : allBids) {
            if (bid.getId().equals(currentBid.getId()))
                bid = currentBid;
            if (!BidMintEnums.DRAFT.equals(bid.getStatus())) {
                double score = ScoreUtils.calculateBidScoreWRTBestBid(bid, proposal.getBestBid());
                bidScores.add(score);
                BidStats bidStats = new BidStats();
                if (Objects.nonNull(bid.getBidStats()))
                    bid.getBidStats().setBidScore(score);
                else {
                    bidStats.setBidScore(score);
                    bid.setBidStats(bidStats);
                }
                ScoreUtils.calculateBidStats(bid, proposal);
                if (bid.getId().equals(currentBid.getId()))
                    currentBid = bid;
            }
        }

        for (Bid bid : allBids) {
            if (bid.getId().equals(currentBid.getId()))
                bid = currentBid;
            if (!BidMintEnums.DRAFT.equals(bid.getStatus())) {
                Double currentBidScore = bid.getBidStats().getBidScore();
                double normalizedScore = ScoreUtils.normalizeBidScore(bidScores, currentBidScore);
                bid.getBidStats().setBidScore(normalizedScore);
                bidMintDaoFactory.getBidDao().save(bid);
            }
        }

    }

    private NotificationTemplate getNsTemplateForBuyer(Bid bid) {
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        List<String> toEmailList = new ArrayList<>();
        Buyer buyer = bidMintDaoFactory.getBuyerDao().findById(bid.getBuyerId());
        toEmailList.add(buyer.getEmailId());
        notificationTemplate.setToEmail(toEmailList);
        return notificationTemplate;
    }

    @Override
    public Mono<BuyerDTO> acceptBid(String bidId) {
        BuyerDTO buyerDTO = new BuyerDTO();
        Bid bid = bidMintDaoFactory.getBidDao().findById(bidId);
        Proposal proposal = bidMintDaoFactory.getProposalDao().findById(bid.getProposalId());
        proposal.setStatus(BidMintEnums.ACCEPTED);
        bid.setStatus(BidMintEnums.ACCEPTED);
        proposal.setSellerId(bid.getSellerId());
        Seller seller = bidMintDaoFactory.getSellerDao().findById(bid.getSellerId());
        Buyer buyer = bidMintDaoFactory.getBuyerDao().findById(bid.getBuyerId());
        NotificationTemplate notificationTemplateBuyer = new NotificationTemplate();
        notificationTemplateBuyer.setToEmail(new ArrayList<String>() {
            {
                add(buyer.getEmailId());
            }
        });
        NotificationTemplate notificationTemplateSeller = new NotificationTemplate();
        notificationTemplateSeller.setToEmail(new ArrayList<String>() {
            {
                add(seller.getEmailId());
            }
        });
        if (notificationServiceProvider.sendNotification(notificationTemplateBuyer, "ABB")
                && notificationServiceProvider.sendNotification(notificationTemplateSeller, "ABS")) {
            buyerDTO.setStatusCode(HttpStatus.OK.value());
            buyerDTO.setMessage("Notification is Triggered");
            return Mono.just(buyerDTO);
        }
        buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        buyerDTO.setMessage("Notification failed");
        return Mono.just(buyerDTO);
    }

    @Override
    public Flux<Bid> getBids(String sellerId) {
        return bidMintDaoFactory.getBidDao().getAllBidsBySellerIdRx(sellerId);
    }

    @Override
    public Flux<Bid> getBids(String sellerId, String status) {
        if (Objects.nonNull(status))
            return bidMintDaoFactory.getBidDao().getBidsBySellerId(sellerId, status);
        else
            return bidMintDaoFactory.getBidDao().getAllBidsBySellerId(sellerId);
    }

    @Override
    public Mono<BidDTO> getBidDetails(String bidId) {
        BidDTO bidDTO = new BidDTO();
        Bid bid = bidMintDaoFactory.getBidDao().findById(bidId);
        bidDTO.setBid(bid);
        bidDTO.setStatusCode(HttpStatus.OK.value());
        bidDTO.setMessage("SUCCESS");
        return Mono.just(bidDTO);
    }

    @Override
    public Flux<Bid> getBidsByProposalId(String proposalId, String topN) {
        List<Bid> allBids = bidMintDaoFactory.getBidDao().getAllBidsByProposalId(proposalId);
        List<Bid> topNBids = new ArrayList<>();
        if (topN == null)
            return Flux.fromIterable(allBids);

        Map<Double, List<String>> map = new TreeMap<>(Collections.reverseOrder());
        for (Bid bid : allBids) {
            if (!BidMintEnums.DRAFT.equals(bid.getStatus())) {
                Double key = bid.getBidStats().getBidScore();
                if (!map.containsKey(key)) {
                    List<String> sameScoreBids = new ArrayList<>();
                    sameScoreBids.add(bid.getId());
                    map.put(key, sameScoreBids);
                } else {
                    List<String> existingBids = map.get(key);
                    existingBids.add(bid.getId());
                    map.put(key, existingBids);
                }
            }
        }
        int n = Integer.parseInt(topN);
        Iterator it = map.entrySet().iterator();
        while (n > 0 && it.hasNext()) {
            Map.Entry<Integer, List<String>> entry = (Map.Entry<Integer, List<String>>) it.next();
            List<String> presentBids = entry.getValue();
            for (String b : presentBids) {
                topNBids.add(bidMintDaoFactory.getBidDao().findById(b));
                n--;
            }
        }
        return Flux.fromIterable(topNBids);
    }

    @Override
    public Mono<BidDTO> mergeBids(String bidIdOne, String bidIdTwo) {
        try {
            BidDTO bidDTO = new BidDTO();
            Bid bidOne = bidMintDaoFactory.getBidDao().findById(bidIdOne);
            Bid bidTwo = bidMintDaoFactory.getBidDao().findById(bidIdTwo);
            Integer mergePercent = bidOne.getPercent() + bidTwo.getPercent();
            if (mergePercent > 100) {
                bidDTO.setMessage("Percent is more than 100. Please recreate a new bid.");
                bidDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return Mono.just(bidDTO);
            }
            bidOne.setPercent(mergePercent);
            bidOne.setAmount(bidOne.getAmount() + bidTwo.getAmount());
            if (bidTwo.getIsMerged()) {
                for (String merge : bidTwo.getMergeList()) {
                    bidOne.getMergeList().add(merge);
                }
            }
            bidOne.getMergeList().add(bidIdTwo);
            bidOne.setIsMerged(true);
            bidTwo.setIsMerged(true);
            bidTwo.setStatus(BidMintEnums.MERGED);
            bidTwo.setMergeId(bidIdOne);
            bidOne.setStatus(BidMintEnums.ACTIVE);
            if (bidOne.getPercent() < 100) {
                bidOne.setStatus(BidMintEnums.PARTIAL);
            }
            mergeBidStats(bidOne, bidTwo);
            Proposal proposal = bidMintDaoFactory.getProposalDao().findById(bidOne.getProposalId());
            getBestBidAndUpdateOtherBids(proposal, bidOne);
            bidMintDaoFactory.getBidDao().save(bidOne);
            bidMintDaoFactory.getBidDao().save(bidTwo);
            bidDTO.setMessage("Merge Successful");
            bidDTO.setStatusCode(HttpStatus.ACCEPTED.value());
            return Mono.just(bidDTO);
        } catch (Exception exception) {
            BidDTO bidDTO = new BidDTO();
            bidDTO.setMessage("Merge failure");
            bidDTO.setStatusCode(HttpStatus.ACCEPTED.value());
            return Mono.just(bidDTO);
        }
    }

    public void mergeBidStats(Bid bidOne, Bid bidTwo) {
        BidStats bidOneStats = bidOne.getBidStats();
        BidStats bidTwoStats = bidTwo.getBidStats();
        Double score = (bidOneStats.getBidScore() * bidOne.getPercent() +
                bidTwoStats.getBidScore() * bidTwo.getPercent()) / 100;
        bidOneStats.setBidScore(score);
        bidOne.setBidStats(bidOneStats);
    }

}
