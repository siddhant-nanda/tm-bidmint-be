package com.turtlemint.verticals.bidmint.bidmint.services;


import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBuyerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Data
@Service
public class BidMintServiceFactory {

    @Autowired
    @Qualifier("buyerService")
    private IBuyerService buyerService;
}
