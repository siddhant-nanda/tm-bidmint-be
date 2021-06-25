package com.turtlemint.verticals.bidmint.bidmint.utils;

import com.turtlemint.verticals.bidmint.bidmint.dao.NotificationTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationServiceProvider {

    @Value("${notification.url}")
    private String notificationUrl;

    @Value("${dashboard.url}")
    private String dashboardUrl;

    public boolean sendNotification(NotificationTemplate notificationTemplate, String flow) {
        if ("Proposal".equalsIgnoreCase(flow)) {
            sendProposalUtils(notificationTemplate);
        } else if ("Bid".equalsIgnoreCase(flow)) {
            sendBidUtils(notificationTemplate);
        } else if ("ABS".equalsIgnoreCase(flow)) {
            sendAcceptBidBuyer(notificationTemplate);
        } else if ("ABB".equalsIgnoreCase(flow)) {
            sendAcceptBidSeller(notificationTemplate);
        }
        String uri = notificationUrl;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, notificationTemplate, Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public void sendProposalUtils(NotificationTemplate notificationTemplate) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Proposal Mail");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "BIDMINT_PROPOSAL_EMAIL");
        contentMap.put("fromName", "Bidmint");
        Map<String, String> templateMappings = new HashMap<>();
        templateMappings.put("deepLink", dashboardUrl);
        contentMap.put("mappings", templateMappings);
        notificationTemplate.setContent(contentMap);
    }

    public void sendBidUtils(NotificationTemplate notificationTemplate) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Bid Mail");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "BIDMINT_BID_EMAIL");
        contentMap.put("fromName", "BidMint");
        Map<String, String> templateMappings = new HashMap<>();
        templateMappings.put("deepLink", dashboardUrl);
        contentMap.put("mappings", templateMappings);
        notificationTemplate.setContent(contentMap);
    }

    public void sendAcceptBidBuyer(NotificationTemplate notificationTemplate) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Accept Bid Mail - Buyer");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "BIDMINT_SUCCESS_EMAIL_BUYER");
        contentMap.put("fromName", "BidMint");
        Map<String, String> templateMappings = new HashMap<>();
        templateMappings.put("deepLink", dashboardUrl);
        contentMap.put("mappings", templateMappings);
        notificationTemplate.setContent(contentMap);
    }

    public void sendAcceptBidSeller(NotificationTemplate notificationTemplate) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Accept Bid Mail - Seller");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "BIDMINT_SUCCESS_EMAIL_SELLER");
        contentMap.put("fromName", "BidMint");
        Map<String, String> templateMappings = new HashMap<>();
        templateMappings.put("deepLink", dashboardUrl);
        contentMap.put("mappings", templateMappings);
        notificationTemplate.setContent(contentMap);
    }


}
