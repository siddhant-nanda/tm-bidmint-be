package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Mono;

public interface IProposalDao {

    Mono<Proposal> getProposalByFilters();
}
