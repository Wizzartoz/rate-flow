package com.rateflow.presentation.feign;

import com.google.gson.JsonObject;
import com.rateflow.presentation.feign.etag.EtagRequestInterceptor;
import com.rateflow.presentation.feign.etag.EtagResponseInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "exchangeRatesClient",
        url = "http://api.exchangeratesapi.io/v1",
        configuration = {GsonDecoder.class, EtagRequestInterceptor.class, EtagResponseInterceptor.class}
)
public interface CurrencyPairsFetch {
    @GetMapping("/latest")
    JsonObject fetchCurrencyPairs(@RequestParam(name = "access_key") String key);
}
