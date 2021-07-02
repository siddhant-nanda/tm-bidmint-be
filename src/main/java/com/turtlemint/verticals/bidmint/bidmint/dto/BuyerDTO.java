package com.turtlemint.verticals.bidmint.bidmint.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuyerDTO extends BidMintDTO {

    Buyer buyer;
}
