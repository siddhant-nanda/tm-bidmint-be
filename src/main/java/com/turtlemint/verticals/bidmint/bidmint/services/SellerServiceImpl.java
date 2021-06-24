package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.*;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.SellerDTO;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.ISellerService;
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


@Service("sellerService")
public class SellerServiceImpl implements ISellerService {

    @Autowired
    BidMintDaoFactory bidMintDaoFactory;

    public Mono<SellerDTO> createSeller(Seller seller) {
        SellerDTO sellerDTO = new SellerDTO();
        if (Objects.isNull(seller.getId())) {
            UUID uuid = UUID.randomUUID();
            String sellerId = uuid.toString();
            seller.setId(sellerId);
            seller.setCreatedAt(Instant.now().getEpochSecond());
        } else {
            seller.setUpdatedAt(Instant.now().getEpochSecond());
        }
        return bidMintDaoFactory.getSellerDao().createSeller(seller).flatMap(
                sellerNew -> {
                    Map<String, Object> sellerMap = new HashMap<>();
                    sellerMap.put("sellerId", sellerNew.getId());
                    sellerDTO.setMeta(sellerMap);
                    sellerDTO.setStatusCode(HttpStatus.CREATED.value());
                    sellerDTO.setMessage("Seller Created");
                    return Mono.just(sellerDTO);
                }
        ).onErrorResume(error -> {
            sellerDTO.setMessage("Seller Not Created");
            sellerDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return Mono.justOrEmpty(sellerDTO);
        });
    }

    @Override
    public Flux<Seller> getSeller(String sellerId) {
        return bidMintDaoFactory.getSellerDao().getAllSellersRx(sellerId);
    }

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
        update.set(TAT, amount);
        update.set(STATUS, BidMintEnums.ACTIVE);
        return bidMintDaoFactory.getBidDao().updateBidById(bidId, update).flatMap(
                updateResult -> {
                    NotificationTemplate notificationTemplate = new NotificationTemplate();
                    List<String> toEmailList = new ArrayList<>();
                    Bid bid = bidMintDaoFactory.getBidDao().findById(bidId);
                    Buyer buyer= bidMintDaoFactory.getBuyerDao().findById(bid.getBuyerId());
                    toEmailList.add(buyer.getEmailId());
                    notificationTemplate.setToEmail(toEmailList);
                    if (NotificationServiceProvider.sendNotification(notificationTemplate, "Bid")) {
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
}

