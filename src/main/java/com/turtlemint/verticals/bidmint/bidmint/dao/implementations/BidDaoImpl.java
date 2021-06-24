package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBidDao;
import org.springframework.stereotype.Repository;

@Repository("bidDao")
public class BidDaoImpl extends AbstractDAOImpl<Bid> implements IBidDao {

    @Override
    public Bid findById(String id) {
        return findById(id, Bid.class);
    }
}
