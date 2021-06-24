package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.core.publisher.Mono;

import java.util.Objects;


public abstract class AbstractDAOImpl<T> {


    @Autowired
    private MongoTemplate               mongoTemplate;


    protected Mono<T> findByIdRx(final Object id, final Class<T> clazz) {
        return Mono.just(Objects.requireNonNull(mongoTemplate.findById(id, clazz)));
    }

    protected T findById(final Object id, final Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    public Mono<T> persistRx(final T t) {
        return Mono.justOrEmpty(mongoTemplate.save(t));
    }
    }

