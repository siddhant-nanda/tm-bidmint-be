package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dto.BidDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBidService {

    Mono<BuyerDTO> acceptBid(String bidId);

    Flux<Bid> getBids(String sellerId);

    Mono<BuyerDTO> createBid(Bid bid);

    Mono<BuyerDTO> publishBid(String bidId, Double amount);

    Flux<Bid> getBids(String sellerId, String status);

    Mono<BidDTO> getBidDetails(String bidId);

    Flux<Bid> getBidsByProposalId(String proposalId);

}
