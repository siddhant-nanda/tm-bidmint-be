package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.ISellerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@Repository("sellerDao")
public class SellerDaoImpl extends AbstractDAOImpl<Seller> implements ISellerDao {

    @Override
    public Mono<Seller> findById(String id) {
        return findByIdRx(id, Seller.class);
    }

    @Override
    public Mono<Seller> createSeller(Seller seller) {
        return persistRx(seller);
    }


}