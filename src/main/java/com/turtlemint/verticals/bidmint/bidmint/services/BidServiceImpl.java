package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.*;
import com.turtlemint.verticals.bidmint.bidmint.dto.BidDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBidService;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import com.turtlemint.verticals.bidmint.bidmint.utils.NotificationServiceProvider;
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
    public Mono<BuyerDTO> publishBid(String bidId, Integer amount) {
        BuyerDTO buyerDTO = new BuyerDTO();
        Update update = new Update();
        update.set(PUBLISHED_AT, Instant.now().getEpochSecond());
        update.set(AMOUNT, amount);
        update.set(STATUS, BidMintEnums.ACTIVE);
        return bidMintDaoFactory.getBidDao().updateBidById(bidId, update).flatMap(
                updateResult -> {
                    NotificationTemplate notificationTemplate = new NotificationTemplate();
                    List<String> toEmailList = new ArrayList<>();
                    Bid bid = bidMintDaoFactory.getBidDao().findById(bidId);
                    Buyer buyer = bidMintDaoFactory.getBuyerDao().findById(bid.getBuyerId());
                    toEmailList.add(buyer.getEmailId());
                    notificationTemplate.setToEmail(toEmailList);
                    if (NotificationServiceProvider.sendNotification(notificationTemplate, "Bid")
                            && proposalService.updateProposalDetails(bid.getProposalId())) {
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
        if (NotificationServiceProvider.sendNotification(notificationTemplateBuyer, "ABB")
                && NotificationServiceProvider.sendNotification(notificationTemplateSeller, "ABS")) {
            buyerDTO.setStatusCode(HttpStatus.OK.value());
            buyerDTO.setMessage("Notification is Triggered");
            return Mono.just(buyerDTO);
        }
        buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        buyerDTO.setMessage("Notification failed");
        return Mono.just(buyerDTO);
    }

    @Override
    public Flux<Bid> getBids(String proposalId) {
        return bidMintDaoFactory.getBidDao().getAllBidsByProposalIdRx(proposalId);
    }

    @Override
    public Flux<Bid> getBids(String sellerId, String status) {

        if (Objects.isNull(status))
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

}
