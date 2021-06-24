package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.BidMintDaoFactory;
import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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
}
