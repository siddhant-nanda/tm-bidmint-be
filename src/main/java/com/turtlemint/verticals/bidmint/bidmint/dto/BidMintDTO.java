package com.turtlemint.verticals.bidmint.bidmint.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BidMintDTO {
    Map<String, Object> meta;
    Integer statusCode;
    String message;
}
