package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Mono;

public interface IBuyerDao {

    Buyer findById(String id);

    Mono<Buyer> findByIdRx(String id);

    Mono<Buyer> createBuyer(Buyer buyer);

    Mono<Proposal> createProposalDao(Proposal proposal);
}
