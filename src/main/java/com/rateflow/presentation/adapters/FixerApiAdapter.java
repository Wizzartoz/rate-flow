package com.rateflow.presentation.adapters;

import com.google.gson.JsonObject;
import com.rateflow.domain.CurrencyPair;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
@Primary
public class FixerApiAdapter implements ExchangeApiAdapter {
    private static final int LIMITER = 6;
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
                    CurrencyPair pairFromBase = CurrencyPair
                            .builder()
                            .from(baseCurrency)
                            .to(from)
                            .rate(BigDecimal.valueOf(rateFromBase).setScale(LIMITER, RoundingMode.HALF_UP).doubleValue())
                            .build();
                    CurrencyPair pairToBase = CurrencyPair
                            .builder()
                            .from(from)
                            .to(baseCurrency)
                            .rate(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(rateFromBase), LIMITER, RoundingMode.HALF_UP).doubleValue())
                            .build();

                    return Flux.just(pairFromBase, pairToBase)
                            .concatWith(Flux.fromIterable(currencies)
                                            .filter(to -> !from.equals(to) && !to.equals(baseCurrency))
                                            .map(to -> CurrencyPair
                                                    .builder()
                                                    .from(from)
                                                    .to(to)
                                                    .rate(getRate(rates, from, to).doubleValue())
                                                    .build())
                            );
                });
    }

    private BigDecimal getRate(JsonObject rates, String from, String to) {
        //The issue is with accuracy because from the API I already receive rounded numbers,
        //and when dividing two rounded numbers, I get a number that is not accurate to a certain digit
        BigDecimal rateFrom = BigDecimal.valueOf(rates.get(from).getAsDouble());
        BigDecimal rateTo = BigDecimal.valueOf(rates.get(to).getAsDouble());
        return rateFrom.divide(rateTo, LIMITER, RoundingMode.HALF_UP);
    }
}
