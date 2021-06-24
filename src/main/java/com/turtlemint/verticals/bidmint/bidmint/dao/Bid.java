package com.turtlemint.verticals.bidmint.bidmint.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "Bid")
public class Bid {

    @Id
    String id;
    String proposalId;
    String sellerId;
    String buyerId;
    Long amount;
    List<Map<String, Object>> proposalAnswers;
    Long createdAt;
    Long updatedAt;
    Long publishedAt;
    BidMintEnums status;
    Double bidScore;
}
