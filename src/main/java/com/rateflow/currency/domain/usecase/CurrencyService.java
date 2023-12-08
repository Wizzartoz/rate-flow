package com.rateflow.currency.domain.usecase;

import com.rateflow.currency.infrastructure.repository.CurrencyPairRepository;
import com.rateflow.currency.domain.entity.CurrencyPair;
import com.rateflow.currency.domain.exception.EmptyCurrencyPairsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CurrencyService {

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
