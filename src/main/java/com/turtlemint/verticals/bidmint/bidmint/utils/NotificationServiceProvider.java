package com.turtlemint.verticals.bidmint.bidmint.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class NotificationServiceProvider {
    public static boolean sendNotification(){
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("type", "EMAIL");
        notificationMap.put("toEmail", new String[]{"siddhant.n@turtlemint.com"});
        notificationMap.put("broker", "yesbank");
        notificationMap.put("clientId", "yesbank");
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("subject", "BidMint Mail");
        contentMap.put("fromEmail", "no-reply@turtlemint.com");
        contentMap.put("templateUrl", "https://s3.ap-south-1.amazonaws.com/message-templates-1/mintpro/templates/null");
        contentMap.put("isMigrated", true);
        contentMap.put("templateCode", "CUSTOMER_CONSENT_MAIL_yesbank");
        contentMap.put("fromName", "yesbank");
        notificationMap.put("content", contentMap);
        final String uri = "https://notifications.ironman.stage.mintpro.in/notifications?broker=yesbank";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, notificationMap, Void.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        }
        return false;
    }
}
