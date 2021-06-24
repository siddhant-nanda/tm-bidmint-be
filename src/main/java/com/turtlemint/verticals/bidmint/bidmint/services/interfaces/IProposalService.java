package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import reactor.core.publisher.Mono;


public interface IProposalService {

    Mono<Proposal> getProposal(String status, String type, String id);
}
