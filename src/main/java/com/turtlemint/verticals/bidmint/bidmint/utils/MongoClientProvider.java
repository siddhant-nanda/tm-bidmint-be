package com.turtlemint.verticals.bidmint.bidmint.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoClientProvider {

    private static final Object LOCK = new Object();
    private static final Logger LOG = LoggerFactory.getLogger("Mongo Connector");

    private MongoClientProvider() {
    }

    public static MongoClient getMongoClient(String masterDbServerAdderss, String slaveDbServerAdderss, Integer dbServerPort) {
        synchronized (LOCK) {
            final MongoClientOptions options = MongoClientOptions.builder().build();
            final List<ServerAddress> serverAddresses = new ArrayList<>();
            try {
                serverAddresses.add(new ServerAddress(masterDbServerAdderss, dbServerPort));
                serverAddresses.add(new ServerAddress(slaveDbServerAdderss, dbServerPort));
                return new MongoClient(serverAddresses, MongoCredential.createCredential("bidmintUser", "bidmint", new char[]{'b'}), options);
            } catch (Exception e) {
                LOG.error("[getMongoClient] Exception occurred {}", e);
            }
        }
        return null;
    }
}
