package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.BidMintDaoFactory;
import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dto.SellerDTO;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


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
}
