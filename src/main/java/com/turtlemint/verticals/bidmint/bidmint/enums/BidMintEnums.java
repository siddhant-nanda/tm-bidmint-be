package com.turtlemint.verticals.bidmint.bidmint.enums;

public enum BidMintEnums {
    DRAFT("draft"),
    PENDING("pending"),
    ACTIVE("active"),
    ACCEPTED("accepted");

    private String status;

    BidMintEnums(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
