package com.turtlemint.verticals.bidmint.bidmint.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.turtlemint.verticals.bidmint.bidmint.enums.BidMintEnums;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "Proposal")
public class Proposal {
    @Id
    String id;
    Long createdAt;
    Long updatedAt;
    Long publishedAt;
    Long turnAroundTime;
    List<ProposalQuestion> proposalQuestions;
    BidMintEnums status;
    String buyerId;
    String sellerId;

}
