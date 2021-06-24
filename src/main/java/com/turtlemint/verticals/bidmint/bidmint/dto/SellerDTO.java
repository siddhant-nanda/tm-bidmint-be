package com.turtlemint.verticals.bidmint.bidmint.dto;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import lombok.Data;

@Data
public class SellerDTO extends BidMintDTO{

    Seller seller;
}
