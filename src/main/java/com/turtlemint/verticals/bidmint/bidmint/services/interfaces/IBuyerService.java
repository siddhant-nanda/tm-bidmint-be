package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBuyerService {

    Mono<BuyerDTO> createBuyer(Buyer buyer);

    Flux<Buyer> getBuyer(String buyerId);

    Mono<BuyerDTO> createProposal(Proposal proposal);

    Mono<BuyerDTO> publishProposal(String proposalId, Long turnAroundTime);
}
