package com.rateflow.currency.presentation.feign.decoder;

import com.google.gson.Gson;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class GsonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, type);
        }
    }
}
