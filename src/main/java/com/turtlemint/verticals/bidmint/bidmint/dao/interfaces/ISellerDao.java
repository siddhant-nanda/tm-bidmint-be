package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ISellerDao {

    Mono<Seller> findByIdRx(String id);

    Seller findById(String id);

    Mono<Seller> createSeller(Seller seller);

    Flux<Seller> getAllSellersRx(String sellerId);

    List<Seller> getAllSellers(String sellerId);
}
