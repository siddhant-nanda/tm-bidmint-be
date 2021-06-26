package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBidDao;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.*;

@Repository("bidDao")
public class BidDaoImpl extends AbstractDAOImpl<Bid> implements IBidDao {

    @Override
    public Bid findById(String id) {
        return findById(id, Bid.class);
    }

    @Override
    public Flux<Bid> getAllBidsBySellerIdRx(String sellerId) {
        if (Objects.nonNull(sellerId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(SELLER_ID).is(sellerId));
            return findAllRx(query, Bid.class);
        }
        return findAllRx(null, Bid.class);
    }

    @Override
    public Flux<Bid> getAllBidsByProposalIdRx(String proposalId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(PROPOSAL_ID).is(proposalId));
        query.addCriteria(Criteria.where(STATUS).is(BidMintEnums.ACTIVE));
        return findAllRx(query, Bid.class);
    }

    @Override
    public List<Bid> getAllBidsByProposalId(String proposalId) {
        if (Objects.nonNull(proposalId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(PROPOSAL_ID).is(proposalId));
            query.addCriteria(Criteria.where(STATUS).ne(BidMintEnums.DRAFT));
            return findAll(query, Bid.class);
        }
        return findAll(null, Bid.class);
    }

    public Mono<Bid> createBidDao(Bid bid) {
        return persistRx(bid);
    }

    @Override
    public Mono<UpdateResult> updateBidById(String bidId, Update update) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(bidId));
        Mono<UpdateResult> updateResultMono = update(query, update, Bid.class);
        return updateResultMono;
    }

    @Override
    public Flux<Bid> getBidsBySellerId(String sellerId, String status) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(SELLER_ID).is(sellerId));
        query.addCriteria(Criteria.where(STATUS).is(status));
        return findAllRx(query, Bid.class);
    }

    @Override
    public Flux<Bid> getAllBidsBySellerId(String sellerId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(sellerId));
        return findAllRx(query, Bid.class);
    }

    @Override
    public void save(Bid bid) {
        persistRx(bid);
    }
}
