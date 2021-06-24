package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.ProposalDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IProposalService {

    Flux<Proposal> getProposals(String status, String type, String id);

    Boolean updateProposalDetails(String proposalId);

    Mono<ProposalDTO> getProposalDetails(String proposalId);
}
