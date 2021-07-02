package com.turtlemint.verticals.bidmint.bidmint.dao;

import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBidDao;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IBuyerDao;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IProposalDao;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.ISellerDao;
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

    @Autowired
    @Qualifier("sellerDao")
    private ISellerDao sellerDao;

    @Autowired
    @Qualifier("proposalDao")
    private IProposalDao proposalDao;

    @Autowired
    @Qualifier("bidDao")
    private IBidDao bidDao;


}
