package com.rateflow.currency.pair.service.fetch.pairs.feature;

import com.rateflow.currency.pair.controller.CurrencyPairsFetch;
import com.rateflow.currency.pair.repository.CurrencyPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Profile("!test")
@Slf4j
public class CurrencyPairFetchScheduler {

    private final CurrencyPairsFetch currencyPairsFetch;
    private final CurrencyPairRepository currencyPairRepository;
    private final ExchangeApiAdapter exchangeApiAdapter;

    @Autowired
    public CurrencyPairFetchScheduler(
            CurrencyPairsFetch currencyPairsFetch,
            CurrencyPairRepository currencyPairRepository,
            ExchangeApiAdapter exchangeApiAdapter
    ) {
        this.currencyPairsFetch = currencyPairsFetch;
        this.currencyPairRepository = currencyPairRepository;
        this.exchangeApiAdapter = exchangeApiAdapter;
    }

    @Value("${custom.key}")
    private String key;

    @Scheduled(fixedRateString = "${custom.scheduledRate}")
    public void getAllCurrencyPair() {
        //TODO make synchronization for scaling via redis

        //TODO  need to add an ETag check

        Mono.fromCallable(() -> currencyPairsFetch.fetchCurrencyPairs(key))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(jsonObject -> log.debug("Currency pairs were obtained"))
                .flatMapMany(exchangeApiAdapter::parsePairs)
                .collectList()
                .doOnNext(pairs -> log.debug("Currency pairs were converted"))
                .flatMap(pairs ->
                        // TODO perhaps it would be better to divide it into packages of 2,000
                        currencyPairRepository.deleteAll()
                                .thenMany(currencyPairRepository.saveAll(pairs))
                                .collectList()
                )
                .doOnNext(savedPairs -> log.debug("All currency pairs have been saved: " + savedPairs.size()))
                .doOnError(error -> log.error("Error updating currency pairs", error))
                .then()
                .doOnSuccess(unused -> log.info("Currency pairs update process completed."))
                .subscribe();
    }
}
