package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.BidMintDaoFactory;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.BUYER;
import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.SELLER;

@Service("proposalService")
public class ProposalServiceImpl implements IProposalService {

    @Autowired
    BidMintDaoFactory bidMintDaoFactory;

    @Override
    public Flux<Proposal> getProposals(String status, String type, String id) {

        if (BUYER.equalsIgnoreCase(type))
            return bidMintDaoFactory.getProposalDao().getProposalsByBuyerId(id, status);
        else if (SELLER.equalsIgnoreCase(type))
            return bidMintDaoFactory.getProposalDao().getProposalsBySellerId(id, status);

        return Flux.just(null);
    }
}
