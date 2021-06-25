package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;


import com.mongodb.client.result.UpdateResult;
import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;


public abstract class AbstractDAOImpl<T> {


    @Autowired
    private MongoTemplate mongoTemplate;


    protected Mono<T> findByIdRx(final Object id, final Class<T> clazz) {
        return Mono.just(Objects.requireNonNull(mongoTemplate.findById(id, clazz)));
    }

    protected T findById(final Object id, final Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    protected Flux<T> findByQuery(final Query query, final Class<T> clazz) {
        final List<T> l = mongoTemplate.find(query, clazz);
        if (l.isEmpty()) {
            return Flux.empty();
        }
        return Flux.fromIterable(l);
    }

    public Mono<T> persistRx(final T t) {
        return Mono.justOrEmpty(mongoTemplate.save(t));
    }

    public Mono<UpdateResult> update(final Query query, final Update update, final Class<T> clazz) {
        return Mono.justOrEmpty(mongoTemplate.updateFirst(query, update, clazz));
    }

    public Mono<Proposal> createProposalDao(Proposal proposal) {
        return Mono.justOrEmpty(mongoTemplate.save(proposal, "Proposal"));
    }

    protected Flux<T> findAllRx(final Query query, final Class<T> clazz) {
        List<T> l;
        if (Objects.nonNull(query)) {
             l = mongoTemplate.find(query, clazz);
        }
        else{
            l = mongoTemplate.findAll(clazz);
        }
        return Flux.fromIterable(l);
    }

    protected List<T> findAll(final Query query, final Class<T> clazz) {
        List<T> l;
        if (Objects.nonNull(query)) {
            l = mongoTemplate.find(query, clazz);
        }
        else {
            l = mongoTemplate.findAll(clazz);
        }
        return l;
    }
}

