package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.*;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBuyerService;
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

@Service("buyerService")
public class BuyerServiceImpl implements IBuyerService {

    @Autowired
    BidMintDaoFactory bidMintDaoFactory;


    public Mono<BuyerDTO> createBuyer(Buyer buyer) {
        BuyerDTO buyerDTO = new BuyerDTO();
        if (Objects.isNull(buyer.getId())) {
            UUID uuid = UUID.randomUUID();
            String buyerId = uuid.toString();
            buyer.setId(buyerId);
            buyer.setCreatedAt(Instant.now().getEpochSecond());
        } else {
            buyer.setUpdatedAt(Instant.now().getEpochSecond());
        }
        return bidMintDaoFactory.getBuyerDao().createBuyer(buyer).flatMap(
                buyerNew -> {
                    Map<String, Object> buyerMap = new HashMap<>();
                    buyerMap.put("buyerId", buyerNew.getId());
                    buyerDTO.setMeta(buyerMap);
                    buyerDTO.setStatusCode(HttpStatus.CREATED.value());
                    buyerDTO.setMessage("Buyer Created");
                    return Mono.just(buyerDTO);
                }
        ).onErrorResume(error -> {
            buyerDTO.setMessage("Buyer Not Created");
            buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return Mono.justOrEmpty(buyerDTO);
        });
    }

    public Mono<BuyerDTO> createProposal(Proposal proposal) {
        BuyerDTO buyerDTO = new BuyerDTO();
        if (Objects.isNull(proposal.getId())) {
            UUID uuid = UUID.randomUUID();
            String proposalId = uuid.toString();
            proposal.setId(proposalId);
            proposal.setCreatedAt(Instant.now().getEpochSecond());
        } else {
            proposal.setUpdatedAt(Instant.now().getEpochSecond());
        }
        proposal.setStatus(BidMintEnums.DRAFT);
        return bidMintDaoFactory.getBuyerDao().createProposalDao(proposal).flatMap(
                proposalNew -> {
                    Map<String, Object> proposalMap = new HashMap<>();
                    proposalMap.put("proposalId", proposalNew.getId());
                    buyerDTO.setMeta(proposalMap);
                    buyerDTO.setStatusCode(HttpStatus.CREATED.value());
                    buyerDTO.setMessage("Proposal Created");
                    return Mono.just(buyerDTO);
                }
        ).onErrorResume(error -> {
            buyerDTO.setMessage("Proposal Not Created");
            buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return Mono.justOrEmpty(buyerDTO);
        });
    }

    public Mono<BuyerDTO> publishProposal(String proposalId, Long turnAroundTime) {
        BuyerDTO buyerDTO = new BuyerDTO();
        Update update = new Update();
        update.set(PUBLISHED_AT, Instant.now().getEpochSecond());
        update.set(TAT, turnAroundTime);
        update.set(STATUS, BidMintEnums.ACTIVE);
        return bidMintDaoFactory.getProposalDao().updateProposalById(proposalId, update).flatMap(
                updateResult -> {
                    NotificationTemplate notificationTemplate = new NotificationTemplate();
                    List<String> toEmailList = new ArrayList<>();
                    List<Seller> sellers = bidMintDaoFactory.getSellerDao().getAllSellers(null);
                    for (Seller seller : sellers) {
                        toEmailList.add(seller.getEmailId());
                    }
                    notificationTemplate.setToEmail(toEmailList);
                    if (NotificationServiceProvider.sendNotification(notificationTemplate, "Proposal")) {
                        buyerDTO.setStatusCode(HttpStatus.OK.value());
                        buyerDTO.setMessage("Proposal is Published and Notification is Triggered");
                        return Mono.just(buyerDTO);
                    }
                    buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    buyerDTO.setMessage("Proposal is Published but notification failed");
                    return Mono.just(buyerDTO);
                }
        ).onErrorResume(error -> {
            buyerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            buyerDTO.setMessage("Proposal is not Published");
            return Mono.just(buyerDTO);
        });
    }


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
            } });
        NotificationTemplate notificationTemplateSeller = new NotificationTemplate();
        notificationTemplateSeller.setToEmail(new ArrayList<String>() {
            {
                add(seller.getEmailId());
            } });
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

    public Flux<Bid> getBids(String proposalId) {
        return bidMintDaoFactory.getBidDao().getAllBidsByProposalId(proposalId);
    }


}