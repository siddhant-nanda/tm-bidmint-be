package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.ISellerDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.BUYER_ID;
import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.PROPOSAL_ID;

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

    @Override
    public Flux<Seller> getAllSellers(String sellerId) {
        if (Objects.nonNull(sellerId)){
            final Query query = new Query();
            query.addCriteria(Criteria.where(PROPOSAL_ID).is(sellerId));
            return findAll(query, Seller.class);
        }
        return findAll(null, Seller.class);
    }


}