package com.turtlemint.verticals.bidmint.bidmint.controllers;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dto.SellerDTO;
import com.turtlemint.verticals.bidmint.bidmint.services.BidMintServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        return bidMintServiceFactory.getSellerService().createSeller(seller).map(buyerDTO -> new ResponseEntity<>(buyerDTO,
                HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
