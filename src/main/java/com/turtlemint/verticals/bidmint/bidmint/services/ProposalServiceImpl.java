package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.BidMintDaoFactory;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("proposalService")
public class ProposalServiceImpl implements IProposalService {

    @Autowired
    BidMintDaoFactory bidMintDaoFactory;

    @Override
    public Mono<Proposal> getProposal(String status, String type, String id) {
        return null;
    }
}
