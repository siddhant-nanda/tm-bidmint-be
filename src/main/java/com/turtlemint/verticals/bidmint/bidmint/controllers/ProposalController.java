package com.turtlemint.verticals.bidmint.bidmint.controllers;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.ProposalDTO;
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
public class ProposalController {

    @Autowired
    BidMintServiceFactory bidMintServiceFactory;

    @RequestMapping(value = "/get-proposals", method = RequestMethod.GET)
    public Flux<ResponseEntity<Proposal>> getProposals(@Valid @RequestParam String status, @Valid @RequestParam String type,
                                                       @Valid @RequestParam String id) {

        return bidMintServiceFactory.getProposalService().getProposals(status,
                type, id).map(proposalDTO -> new ResponseEntity<>(proposalDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

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

    @RequestMapping(value = "/get-proposal-info", method = RequestMethod.GET)
    public Mono<ResponseEntity<ProposalDTO>> publishProposal(@RequestParam String proposalId) {
        return bidMintServiceFactory.getProposalService().getProposalDetails(proposalId).map(proposalDTO -> new ResponseEntity<>(proposalDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
