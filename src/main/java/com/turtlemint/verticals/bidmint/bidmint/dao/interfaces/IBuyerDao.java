package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import reactor.core.publisher.Mono;

public interface IBuyerDao {

    Mono<Buyer> findById(String id);

    Mono<Buyer> createBuyer(Buyer buyer);
}
