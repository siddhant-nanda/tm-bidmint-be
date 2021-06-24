package com.turtlemint.verticals.bidmint.bidmint.utils;

import com.turtlemint.verticals.bidmint.bidmint.dao.NotificationTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class NotificationServiceProvider {
    public static boolean sendNotification(NotificationTemplate notificationTemplate, String flow) {
        if("Proposal".equalsIgnoreCase(flow)){
            sendProposalUtils(notificationTemplate);
        }
        else if("Bid".equalsIgnoreCase(flow)){
            sendBidUtils(notificationTemplate);
        }
        final String uri = "https://notifications.ironman.stage.mintpro.in/notifications?broker=yesbank";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, notificationTemplate, Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public static void sendProposalUtils(NotificationTemplate notificationTemplate){
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Proposal Mail");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "CUSTOMER_CONSENT_MAIL_yesbank");
        contentMap.put("fromName", "yesbank");
        notificationTemplate.setContentMap(contentMap);
    }

    public static void sendBidUtils(NotificationTemplate notificationTemplate){
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Bid Mail");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "CUSTOMER_CONSENT_MAIL_yesbank");
        contentMap.put("fromName", "yesbank");
        notificationTemplate.setContentMap(contentMap);
    }


}
