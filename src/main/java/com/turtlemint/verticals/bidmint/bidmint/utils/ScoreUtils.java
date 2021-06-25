package com.turtlemint.verticals.bidmint.bidmint.utils;

import com.turtlemint.verticals.bidmint.bidmint.dao.Bid;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dto.BidStats;

import java.util.List;
import java.util.Map;

public class ScoreUtils {

    public static void calculateBidStats(Bid currentBid, Proposal proposal) {
        BidStats bidStats = new BidStats();
        if (currentBid.getBidStats()!=null){
            bidStats = currentBid.getBidStats();
        }
        Double excessAmt = proposal.getAvgBidAmount() - currentBid.getAmount();
        bidStats.setExcessAmount(excessAmt);
        Integer agreed = proposal.getAvgAgreementOnQuestions() - currentBid.getAgreementOnQuestions();
        bidStats.setAgreementOnQuestions(agreed);
        currentBid.setBidStats(bidStats);
    }

    // calculate bid score with respect to current bid
    public static double calculateBidScoreWRTCurrentBid(Bid currentBid, Bid bestBid) {

        double scoreFactor = 0.9;
        double fractionAmount = (100 / currentBid.getAmount()) * bestBid.getAmount();
        double fractionAgreement = (100 / currentBid.getAgreementOnQuestions()) * bestBid.getAgreementOnQuestions();

        /*
        A+B =  A = if agreement increases more score should be deducted
               B = if amount increases more score should be deducted
         */
        return (scoreFactor * -(fractionAgreement)) + ((1 - scoreFactor) * (-fractionAmount));

    }


    public static Integer computeAgreementOnQuestions(Map<String, Object> questionSet1, Map<String, Object> questionSet2) {
        Integer agreed = 0;
        for (Map.Entry mapElement : questionSet1.entrySet()) {
            String key = (String) mapElement.getKey();
            Object val1 = mapElement.getValue();
            Object val2 = questionSet2.get(key);
            if (val1.equals(val2))
                agreed++;
        }
        return agreed;
    }

}
