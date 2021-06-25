package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IProposalDao;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.*;

@Repository("proposalDao")
public class ProposalDaoImpl extends AbstractDAOImpl<Proposal> implements IProposalDao {

    @Override
    public Flux<Proposal> getProposalsByBuyerId(String buyerId, String status) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(BUYER_ID).is(buyerId));
        if (status != null) {
            query.addCriteria(Criteria.where(STATUS).is(status));
        }
        return findByQuery(query, Proposal.class);
    }

    @Override
    public Flux<Proposal> getProposalsBySellerId(String sellerId, String status) {
        final Query query = new Query();
        if (sellerId != null) {
            query.addCriteria(Criteria.where(SELLER_ID).is(sellerId));
        }
        if (!BidMintEnums.DRAFT.getStatus().equalsIgnoreCase(status)) {
            query.addCriteria(Criteria.where(STATUS).is(status));
        }
        else {
            query.addCriteria(Criteria.where(STATUS).ne(BidMintEnums.DRAFT));
        }
        return findByQuery(query, Proposal.class);
    }

    @Override
    public Mono<UpdateResult> updateProposalById(String proposalId, Update update) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(proposalId));
        return update(query, update, Proposal.class);
    }

    @Override
    public Proposal findById(String id) {
        return findById(id, Proposal.class);
    }

    @Override
    public void save(Proposal proposal) {
        persistRx(proposal);
    }


    @Override
    public Flux<Proposal> getAllProposals(String proposalId) {
        if (Objects.nonNull(proposalId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(ID).is(proposalId));
            return findAllRx(query, Proposal.class);
        }
        return findAllRx(null, Proposal.class);
    }
}
