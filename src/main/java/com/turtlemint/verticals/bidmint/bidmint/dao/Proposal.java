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
@Document(collection = "Proposal")
public class Proposal {
    @Id
    String id;
    String name;
    Long createdAt;
    Long updatedAt;
    Long publishedAt;
    Long turnAroundTime;
    List<Map<String, Object>> proposalQuestions;
    BidMintEnums status;
    String buyerId;
    String sellerId;
    Long minimumAmount = Long.MAX_VALUE;
    Integer numberOfParticipants;
    Double bestBidScore = Double.MIN_VALUE;

}
