package com.turtlemint.verticals.bidmint.bidmint.services;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.BidMintDaoFactory;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.ProposalDTO;
import com.turtlemint.verticals.bidmint.bidmint.services.interfaces.IProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.*;

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

    @Override
    public Boolean updateProposalDetails(String proposalId) {
        try {
            Proposal proposal = bidMintDaoFactory.getProposalDao().findById(proposalId);
            List<Bid> bids = bidMintDaoFactory.getBidDao().getAllBidsByProposalId(proposalId);
            Update update = new Update();
            update.set(NO_OF_PARTICIPANTS, bids.size());
            long minimum = proposal.getMinimumAmount();
            double maximum = proposal.getBestBidScore();
            for (Bid bid : bids) {
                if (bid.getAmount() < minimum) {
                    minimum = bid.getAmount();
                }
                if (bid.getBidScore() > maximum) {
                    maximum = bid.getBidScore();
                }
            }
            update.set(MIN_AMOUNT, minimum);
            update.set(BID_SCORE, maximum);
            bidMintDaoFactory.getProposalDao().updateProposalById(proposalId, update);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    @Override
    public Mono<ProposalDTO> getProposalDetails(String proposalId) {
        ProposalDTO proposalDTO = new ProposalDTO();
        Proposal proposal = bidMintDaoFactory.getProposalDao().findById(proposalId);
        proposalDTO.setProposal(proposal);
        proposalDTO.setStatusCode(HttpStatus.OK.value());
        proposalDTO.setMessage("SUCCESS");
        return Mono.just(proposalDTO);
    }
}
