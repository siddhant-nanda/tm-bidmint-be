package com.turtlemint.verticals.bidmint.bidmint.dao;

import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class BidMintDaoFactory {

    @Autowired
    @Qualifier("buyerDao")
    private IBuyerDao buyerDao;
}
