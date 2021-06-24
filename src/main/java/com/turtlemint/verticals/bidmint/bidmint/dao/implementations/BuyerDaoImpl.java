package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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

}
