package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Flux;


public interface IProposalService {

    Flux<Proposal> getProposals(String status, String type, String id);
}
