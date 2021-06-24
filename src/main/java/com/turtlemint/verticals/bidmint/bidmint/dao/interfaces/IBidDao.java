package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import reactor.core.publisher.Flux;

public interface IBidDao {
    Bid findById(String id);

    Flux<Bid> getAllBidsByProposalId(String proposalId);

}
