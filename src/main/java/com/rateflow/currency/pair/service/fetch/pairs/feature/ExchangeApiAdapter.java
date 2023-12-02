package com.rateflow.currency.pair.service.fetch.pairs.feature;

import com.google.gson.JsonObject;
import com.rateflow.currency.pair.entity.CurrencyPair;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

/**
 * This adapter is used for the currency retrieval API and allows the application to be independent of the provider.
 * Otherwise, if the provider changes, the application would have to be rewritten
 */

@Validated
public interface ExchangeApiAdapter {
    /**
     * A method that converts JSON into objects.
     * @param jsonObject - JSON received from the provider.
     * @return list of currency pairs.
     */

    Flux<CurrencyPair> parsePairs(@NotNull JsonObject jsonObject);
}
