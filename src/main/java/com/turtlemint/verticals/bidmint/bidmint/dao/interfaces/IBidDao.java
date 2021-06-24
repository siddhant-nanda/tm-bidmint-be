package com.turtlemint.verticals.bidmint.bidmint.dao.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;

public interface IBidDao {
    Bid findById(String id);
}
