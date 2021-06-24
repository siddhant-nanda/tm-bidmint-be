package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

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


}
