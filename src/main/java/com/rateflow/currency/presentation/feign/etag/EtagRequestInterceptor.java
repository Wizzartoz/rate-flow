package com.rateflow.currency.presentation.feign.etag;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EtagRequestInterceptor implements RequestInterceptor {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    @Override
    public void apply(RequestTemplate template) {
        //The interceptor is not asynchronous
        String etag = reactiveStringRedisTemplate.opsForValue().get("etag").block();
            if (etag != null) {
                template.header("If-None-Match", etag);
        }
    }
}
