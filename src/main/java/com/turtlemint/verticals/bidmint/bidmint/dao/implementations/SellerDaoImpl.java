package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.ISellerDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.turtlemint.verticals.bidmint.bidmint.constants.BidMintConstants.ID;

@Repository("sellerDao")
public class SellerDaoImpl extends AbstractDAOImpl<Seller> implements ISellerDao {

    @Override
    public Mono<Seller> findByIdRx(String id) {
        return findByIdRx(id, Seller.class);
    }

    @Override
    public Seller findById(String id) {
        return findById(id, Seller.class);
    }

    @Override
    public Mono<Seller> createSeller(Seller seller) {
        return persistRx(seller);
    }

    @Override
    public Flux<Seller> getAllSellersRx(String sellerId) {
        if (Objects.nonNull(sellerId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(ID).is(sellerId));
            return findAllRx(query, Seller.class);
        }
        return findAllRx(null, Seller.class);
    }

    public List<Seller> getAllSellers(String sellerId) {
        if (Objects.nonNull(sellerId)) {
            final Query query = new Query();
            query.addCriteria(Criteria.where(ID).is(sellerId));
            return findAll(query, Seller.class);
        }
        return findAll(null, Seller.class);
    }

    @Override
    public Mono<Seller> getSellerByEmailRx(String emailId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("emailId").is(emailId));
        return Mono.justOrEmpty(findOneByQuery(query, Seller.class).get(0));
    }


}