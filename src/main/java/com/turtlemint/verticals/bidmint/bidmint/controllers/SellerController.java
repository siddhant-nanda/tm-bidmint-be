package com.turtlemint.verticals.bidmint.bidmint.controllers;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dto.BuyerDTO;
import com.turtlemint.verticals.bidmint.bidmint.dto.SellerDTO;
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
public class SellerController {

    @Autowired
    BidMintServiceFactory bidMintServiceFactory;

    @RequestMapping(value = "/create-seller", method = RequestMethod.POST)
    public Mono<ResponseEntity<SellerDTO>> createSeller(@Valid @RequestBody Seller seller) {

        return bidMintServiceFactory.getSellerService().createSeller(seller).map(sellerDTO -> new ResponseEntity<>(sellerDTO,
                HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/get-seller", method = RequestMethod.GET)
    public Flux<ResponseEntity<Seller>> getSeller(@Valid @RequestParam String sellerId) {

        return bidMintServiceFactory.getSellerService().getSeller(sellerId).map(buyerDTO -> new ResponseEntity<>(buyerDTO,
                HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/save-bid", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> saveBid(@RequestBody Bid bid) {
        return bidMintServiceFactory.getSellerService().createBid(bid).map(bidDTO -> new ResponseEntity<>(bidDTO, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/publish-bid", method = RequestMethod.POST)
    public Mono<ResponseEntity<BuyerDTO>> publishBid(@RequestParam String bidId,
                                                          @RequestParam Integer amount) {
        return bidMintServiceFactory.getSellerService().publishBid(bidId, amount).map(bid -> new ResponseEntity<>(bid, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
