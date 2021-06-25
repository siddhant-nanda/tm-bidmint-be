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

    public static double calculateBidScore(Bid currentBid, Bid bestBid) {

        double scoreFactor = 0.9;
        int percentAmount = (bestBid.getAmount().intValue() * 100) / currentBid.getAmount().intValue();
        int percentAgreement = (currentBid.getAgreementOnQuestions() * 100) / bestBid.getAgreementOnQuestions();
        return ((scoreFactor) * percentAgreement) + ((1 - scoreFactor) * percentAmount);

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
