package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProposalDao {

    Flux<Proposal> getProposalsByBuyerId(String buyerId, String status);

    Flux<Proposal> getProposalsBySellerId(String sellerId, String status);

    Flux<Proposal> getAllProposals(String proposalId);

    Mono<UpdateResult> updateProposalById(String proposalId, Update update);

    Proposal findById(String id);

    void save(Proposal proposal);


}
