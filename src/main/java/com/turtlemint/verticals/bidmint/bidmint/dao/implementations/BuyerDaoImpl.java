package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.PROPOSAL_ID;

@Repository("buyerDao")
public class BuyerDaoImpl extends AbstractDAOImpl<Buyer> implements IBuyerDao {

    @Override
    public Mono<Buyer> findById(String id) {
        return findByIdRx(id, Buyer.class);
    }

    @Override
    public Mono<Buyer> createBuyer(Buyer buyer) {
        return persistRx(buyer);
    }

    @Override
    public Mono<UpdateResult> updateProposalById(String proposalId, Update update) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(PROPOSAL_ID).is(proposalId));
        Mono<UpdateResult> updateResultMono = updateProposal(query, update, Proposal.class);
        return updateResultMono;
    }
}
