package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.ID;

@Repository("buyerDao")
public class BuyerDaoImpl extends AbstractDAOImpl<Buyer> implements IBuyerDao {

    @Override
    public Buyer findById(String id) {
        return findById(id, Buyer.class);
    }

    @Override
    public Mono<Buyer> findByIdRx(String id) {
        return findByIdRx(id, Buyer.class);
    }

    @Override
    public Mono<Buyer> createBuyer(Buyer buyer) {
        return persistRx(buyer);
    }

    @Override
    public Flux<Buyer> getAllBuyersRx(String buyerId) {
        if (Objects.nonNull(buyerId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(ID).is(buyerId));
            return findAllRx(query, Buyer.class);
        }
        return findAllRx(null, Buyer.class);
    }

    @Override
    public Mono<Buyer> getBuyerByEmailRx(String emailId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("emailId").is(emailId));
        return Mono.justOrEmpty(findOneByQuery(query, Buyer.class).get(0));
    }
}
