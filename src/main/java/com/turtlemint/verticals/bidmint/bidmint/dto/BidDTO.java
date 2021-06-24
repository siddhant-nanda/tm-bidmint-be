package com.turtlemint.verticals.bidmint.bidmint.dto;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import lombok.Data;

@Data
public class BidDTO extends BidMintDTO {
    Bid bid;
}
