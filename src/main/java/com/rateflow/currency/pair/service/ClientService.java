package com.rateflow.currency.pair.service;

import com.rateflow.currency.pair.entity.CurrencyPair;
import com.rateflow.exception.EmptyCurrencyPairsException;
import com.rateflow.currency.pair.repository.CurrencyPairRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ClientService {

    private CurrencyPairRepository currencyPairRepository;

    public Flux<CurrencyPair> getCurrencyPairs() {
        return currencyPairRepository.findAll()
                .switchIfEmpty(Flux.error(new EmptyCurrencyPairsException()));
    }

    public Flux<CurrencyPair> getCurrencyPairByBase(String base) {
        return currencyPairRepository.findByFrom(base)
                .switchIfEmpty(Flux.error(new EmptyCurrencyPairsException()));
    }

    public Mono<CurrencyPair> getRate(String from, String to) {
        return currencyPairRepository.findByFromAndTo(from, to)
                .switchIfEmpty(Mono.error(new EmptyCurrencyPairsException()));
    }
}
