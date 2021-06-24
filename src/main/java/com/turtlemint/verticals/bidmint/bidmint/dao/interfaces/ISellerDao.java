package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import reactor.core.publisher.Mono;

public interface ISellerDao {

    Mono<Seller> findById(String id);

    Mono<Seller> createSeller(Seller seller);
}
