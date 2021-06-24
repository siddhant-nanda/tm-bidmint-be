package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBidDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.PROPOSAL_ID;

@Repository("bidDao")
public class BidDaoImpl extends AbstractDAOImpl<Bid> implements IBidDao {

    @Override
    public Bid findById(String id) {
        return findById(id, Bid.class);
    }

    @Override
    public Flux<Bid> getAllBidsByProposalId(String proposalId) {
        if (Objects.nonNull(proposalId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(PROPOSAL_ID).is(proposalId));
            return findAllRx(query, Bid.class);
        }
        return findAllRx(null, Bid.class);
    }
}
