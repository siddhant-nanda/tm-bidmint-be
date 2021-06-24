package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBidDao {
    Bid findById(String id);

    Flux<Bid> getAllBidsByProposalId(String proposalId);

    Mono<Bid> createBidDao(Bid bid);

    Mono<UpdateResult> updateBidById(String bidId, Update update);

    Flux<Bid> getBidsBySellerId(String sellerId, String status);

    Flux<Bid> getAllBidsBySellerId(String sellerId);

}
