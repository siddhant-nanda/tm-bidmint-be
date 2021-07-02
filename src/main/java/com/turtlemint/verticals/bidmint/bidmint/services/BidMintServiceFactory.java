package com.turtlemint.verticals.bidmint.bidmint.services;


import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBidService;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IBuyerService;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.ISellerService;
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

    @Autowired
    @Qualifier("sellerService")
    private ISellerService sellerService;

    @Autowired
    @Qualifier("proposalService")
    private IProposalService proposalService;

    @Autowired
    @Qualifier("bidService")
    private IBidService bidService;
}
