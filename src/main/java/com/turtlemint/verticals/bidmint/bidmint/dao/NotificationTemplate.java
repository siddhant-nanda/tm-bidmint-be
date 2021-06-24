package com.turtlemint.verticals.bidmint.bidmint.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationTemplate {

    String type = "EMAIL";
    List<String> toEmail;
    String broker = "yesbank";
    String clientId = "dummy";
    Map<String, Object> content;
}
