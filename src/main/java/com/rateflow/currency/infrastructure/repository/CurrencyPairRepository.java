package com.rateflow.currency.infrastructure.repository;

import com.rateflow.currency.domain.entity.CurrencyPair;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyPairRepository extends ReactiveMongoRepository<CurrencyPair, String> {
    @Query("{'$and': [{'from': ?0}, {'to': ?1}]}")
    Mono<CurrencyPair> findByFromAndTo(String from, String to);
    Flux<CurrencyPair> findByFrom(String base);
}
