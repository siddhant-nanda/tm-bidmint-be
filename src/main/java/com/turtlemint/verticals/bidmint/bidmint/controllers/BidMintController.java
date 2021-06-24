package com.turtlemint.verticals.bidmint.bidmint.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.BROKER;

@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/api/bidmint/v1")
public class BidMintController {

    @RequestMapping(value = "/getProposal", method = RequestMethod.GET)
    public Mono<String> getProposal() {
        return Mono.just(BROKER);
    }
}
