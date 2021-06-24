package com.turtlemint.verticals.bidmint.bidmint.controllers;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.services.BidMintServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/api/bidmint/v1")
public class BidController {

    @Autowired
    BidMintServiceFactory bidMintServiceFactory;


    @RequestMapping(value = "/get-bids", method = RequestMethod.GET)
    public Flux<ResponseEntity<Bid>> getBids(@RequestParam String proposalId) {
        return bidMintServiceFactory.getBidService().getBids(proposalId).map(bid -> new ResponseEntity<>(bid, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/save-bid", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> saveBid(@RequestBody Bid bid) {
        return bidMintServiceFactory.getBidService().createBid(bid).map(bidDTO -> new ResponseEntity<>(bidDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/publish-bid", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> publishBid(@RequestParam String bidId,
                                                     @RequestParam Integer amount) {
        return bidMintServiceFactory.getBidService().publishBid(bidId, amount).map(bid -> new ResponseEntity<>(bid, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/accept-bid", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> acceptBid(@RequestParam String bidId) {
        return bidMintServiceFactory.getBidService().acceptBid(bidId).map(buyerDTO -> new ResponseEntity<>(buyerDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/get-bids", method = RequestMethod.GET)
    public Flux<ResponseEntity<Proposal>> getBids(@Valid @RequestParam String sellerId, @Valid @RequestParam String status) {

        return bidMintServiceFactory.getBidService().getBids(sellerId).map(proposalDTO -> new ResponseEntity<>(proposalDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
