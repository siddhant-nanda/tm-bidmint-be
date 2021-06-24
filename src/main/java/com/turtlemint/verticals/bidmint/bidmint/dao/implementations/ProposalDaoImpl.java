package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IProposalDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.*;

@Repository("proposalDao")
public class ProposalDaoImpl extends AbstractDAOImpl<Proposal> implements IProposalDao {

    @Override
    public Flux<Proposal> getProposalsByBuyerId(String buyerId, String status) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(BUYER_ID).is(buyerId));
        query.addCriteria(Criteria.where(STATUS).is(status));
        return findByQuery(query, Proposal.class);
    }

    @Override
    public Flux<Proposal> getProposalsBySellerId(String sellerId, String status) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(SELLER_ID).is(sellerId));
        query.addCriteria(Criteria.where(STATUS).is(status));
        return findByQuery(query, Proposal.class);
    }
}
