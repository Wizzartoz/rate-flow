package com.rateflow.currency.pair.service.fetch.pairs.feature;

import com.google.gson.JsonObject;
import com.rateflow.currency.pair.entity.CurrencyPair;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.Set;

@Service
@Primary
public class FixerApiAdapter implements ExchangeApiAdapter {
    @Override
    public Flux<CurrencyPair> parsePairs(JsonObject jsonObject) {
        String baseCurrency = jsonObject.get("base").getAsString();
        JsonObject rates = jsonObject.getAsJsonObject("rates");

        //TODO the algorithm has complexity O(n^2), it can be improved
        Set<String> currencies = rates.keySet();
        return Flux.fromIterable(currencies)
                .filter(from -> !from.equals(baseCurrency))
                .flatMap(from -> {
                    double rateFromBase = rates.get(from).getAsDouble();
                    CurrencyPair pairFromBase = CurrencyPair.builder().from(baseCurrency).to(from).rate(rateFromBase).build();
                    CurrencyPair pairToBase = CurrencyPair.builder().from(from).to(baseCurrency).rate(1 / rateFromBase).build();

                    return Flux.just(pairFromBase, pairToBase)
                            .concatWith(
                                    Flux.fromIterable(currencies)
                                            .filter(to -> !from.equals(to) && !to.equals(baseCurrency))
                                            .map(to -> CurrencyPair.builder().from(from).to(to).rate(getRate(rates, from, to)).build())
                            );
                });
    }

    private double getRate(JsonObject rates, String from, String to) {
        //TODO may be a problem with accuracy, it is better to limit the decimal places and use BigDecimal
        //The issue is with accuracy because from the API I already receive rounded numbers,
        //and when dividing two rounded numbers, I get a number that is not accurate to a certain digit
        double rateFrom = rates.get(from).getAsDouble();
        double rateTo = rates.get(to).getAsDouble();
        return rateFrom / rateTo;
    }
}
