package com.rateflow.currency.presentation.controller;

import com.rateflow.currency.presentation.dto.response.CurrencyPairResponseDto;
import com.rateflow.currency.domain.usecase.CurrencyService;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/currency-pairs")
@AllArgsConstructor
public class CurrencyController {

    private CurrencyService currencyService;

    @GetMapping
    public Flux<CurrencyPairResponseDto> getAllCurrencyPairs() {
        return currencyService.getCurrencyPairs().map(currencyPair ->
                CurrencyPairResponseDto.builder().
                        id(currencyPair.getId()).
                        from(currencyPair.getFrom()).
                        to(currencyPair.getTo()).
                        symbol(currencyPair.getSymbol()).
                        rate(currencyPair.getRate()).
                        build());
    }

    @GetMapping("/base/{base}")
    public Flux<CurrencyPairResponseDto> getCurrencyPairsByBase(
            @PathVariable String base
    ) {
        return currencyService.getCurrencyPairByBase(base).map(currencyPair ->
                CurrencyPairResponseDto.builder().
                        id(currencyPair.getId()).
                        from(currencyPair.getFrom()).
                        to(currencyPair.getTo()).
                        symbol(currencyPair.getSymbol()).
                        rate(currencyPair.getRate()).
                        build());
    }

    @GetMapping("/rate")
    public Mono<CurrencyPairResponseDto> getRate(
            @RequestParam @NotEmpty String from,
            @RequestParam @NotEmpty String to
    ) {
        return currencyService.getRate(from, to).map(currencyPair ->
                CurrencyPairResponseDto.builder().
                        id(currencyPair.getId()).
                        from(currencyPair.getFrom()).
                        to(currencyPair.getTo()).
                        symbol(currencyPair.getSymbol()).
                        rate(currencyPair.getRate()).
                        build());
    }
}
