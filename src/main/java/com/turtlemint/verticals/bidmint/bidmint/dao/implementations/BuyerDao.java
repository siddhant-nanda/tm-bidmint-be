package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Buyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class BuyerDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Resource(name = "mongoTemplateMap")
    private Map<String, MongoTemplate> mongoTemplateMap;

    private MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    private MongoTemplate determineMongoTemplate(final String broker, final Class<? extends Object> clazz) {
        final String collectionName = getMongoTemplate().getCollectionName(clazz);
        return mongoTemplateMap.get(broker);
    }

    public Mono<Buyer> createBuyerDao(String broker, Buyer buyer) {
        return Mono.justOrEmpty(determineMongoTemplate(broker, buyer.getClass()).save(buyer));
    }

}
