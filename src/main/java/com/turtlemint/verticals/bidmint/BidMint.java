package com.turtlemint.verticals.bidmint;

import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.HashMap;
import java.util.Map;

import static com.turtlemint.verticals.bidmint.bidmint.utils.MongoClientProvider.getMongoClient;

@Slf4j
@SpringBootApplication
public class BidMint {

    private static final Logger LOG = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public static void main(String[] args) {
        new SpringApplicationBuilder(BidMint.class).run(args);
    }

    @Bean(name = "mongoTemplateMap")
    public Map<String, MongoTemplate> getMongoTemplate() {
        LOG.info("[getMongoTemplate] Creating mongo template");
        final Map<String, MongoTemplate> templateMap = new HashMap<>();
        MongoDbFactory mongoDbFactory = mongoDbmongoDbFactory("localhost", "localhost", 27017);
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        templateMap.put("bidmint", new MongoTemplate(mongoDbFactory, converter));
        return templateMap;
    }

    private MongoDbFactory mongoDbmongoDbFactory(String masterDbServerAddress, String slaveDbServerAddress, Integer dbServerPort) {
        final MongoClient mongoClient = getMongoClient(masterDbServerAddress, slaveDbServerAddress, dbServerPort);
        if (mongoClient == null) throw new IllegalStateException("Mongo client is null!");
        return new SimpleMongoDbFactory(mongoClient, "bidmint");
    }
}
