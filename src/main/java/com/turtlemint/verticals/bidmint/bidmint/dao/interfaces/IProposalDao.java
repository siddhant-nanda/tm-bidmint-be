package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Flux;

public interface IProposalDao {

    Flux<Proposal> getProposalsByBuyerId(String buyerId, String status);

    Flux<Proposal> getProposalsBySellerId(String sellerId, String status);

    Flux<Proposal> getAllProposals(String proposalId);
}
