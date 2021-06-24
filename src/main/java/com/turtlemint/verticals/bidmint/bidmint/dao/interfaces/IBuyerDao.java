package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

public interface IBuyerDao {

    Mono<Buyer> findById(String id);

    Mono<Buyer> createBuyer(Buyer buyer);

    Mono<UpdateResult> updateProposalById(String proposalId, Update update);

    Mono<Proposal> createProposalDao(Proposal proposal);
}
