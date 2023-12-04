package com.rateflow.presentation.feign.etag;

import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EtagResponseInterceptor implements ResponseInterceptor {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Override
    public Object aroundDecode(InvocationContext invocationContext) {
        Response response = invocationContext.response();
        if (response.status() >= 200 && response.status() < 300) {
            response.headers().get("Etag").stream().findFirst()
                    .ifPresent(tag -> reactiveStringRedisTemplate.opsForValue().set("etag", tag).subscribe());
        }
        return invocationContext.proceed();
    }
}
