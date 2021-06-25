package com.turtlemint.verticals.bidmint.bidmint.dto;

import lombok.Data;

@Data
public class BidStats {


    // Avg excess amount from existing bids
    Double excessAmount;

    // Avg questions on which there is agreement from all bidders
    Integer agreementOnQuestions;

    // No of bids already on this proposal
    Integer runningBids;

    Double bidScore;

}
