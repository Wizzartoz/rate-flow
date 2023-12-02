package com.rateflow.currency.pair.controller;

import com.google.gson.JsonObject;
import com.rateflow.currency.pair.service.fetch.pairs.feature.GsonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "exchangeRatesClient",
        url = "http://api.exchangeratesapi.io/v1",
        configuration = GsonDecoder.class
)
public interface CurrencyPairsFetch {
    @GetMapping("/latest")
    JsonObject fetchCurrencyPairs(@RequestParam(name = "access_key") String key);
}
