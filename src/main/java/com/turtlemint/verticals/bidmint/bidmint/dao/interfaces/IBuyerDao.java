package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBuyerDao {

    Buyer findById(String id);

    Mono<Buyer> findByIdRx(String id);

    Mono<Buyer> createBuyer(Buyer buyer);

    Flux<Buyer> getAllBuyersRx(String buyerId);

    Mono<Proposal> createProposalDao(Proposal proposal);

    Mono<Buyer> getBuyerByEmailRx(String emailId);
}
