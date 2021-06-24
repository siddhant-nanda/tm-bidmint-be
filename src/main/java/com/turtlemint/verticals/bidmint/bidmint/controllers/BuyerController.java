package com.turtlemint.verticals.bidmint.bidmint.controllers;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
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

@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/api/bidmint/v1")
public class BuyerController {

    @Autowired
    BidMintServiceFactory bidMintServiceFactory;

    @RequestMapping(value = "/create-buyer", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> createBuyer(@RequestBody Buyer buyer) {
        return bidMintServiceFactory.getBuyerService().createBuyer(buyer).map(buyerDTO -> new ResponseEntity<>(buyerDTO,
                HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @RequestMapping(value = "/save-proposal", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> saveProposal(@RequestBody Proposal proposal) {
        return bidMintServiceFactory.getBuyerService().createProposal(proposal).map(buyerDTO -> new ResponseEntity<>(buyerDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/publish-proposal", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> publishProposal(@RequestParam String proposalId,
                                                          @RequestParam Long turnAroundTime) {
        return bidMintServiceFactory.getBuyerService().publishProposal(proposalId, turnAroundTime).map(buyerDTO -> new ResponseEntity<>(buyerDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
