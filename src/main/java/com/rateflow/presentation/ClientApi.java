package com.rateflow.presentation;

import com.rateflow.presentation.dtos.CurrencyPairResponseDto;
import com.rateflow.usecases.ClientService;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/currency-pairs")
@AllArgsConstructor
public class ClientApi {

    private ClientService clientService;

    @GetMapping
    public Flux<CurrencyPairResponseDto> getAllCurrencyPairs() {
        return clientService.getCurrencyPairs().map(currencyPair ->
                CurrencyPairResponseDto.builder().
                        id(currencyPair.getId()).
                        from(currencyPair.getFrom()).
                        to(currencyPair.getTo()).
                        symbol(currencyPair.getSymbol()).
                        rate(currencyPair.getRate()).
                        build());
    }

    @GetMapping("/base/{base}")
    public Flux<CurrencyPairResponseDto> getCurrencyPairsByBase(@PathVariable String base) {
        return clientService.getCurrencyPairByBase(base).map(currencyPair ->
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
        return clientService.getRate(from, to).map(currencyPair ->
                CurrencyPairResponseDto.builder().
                        id(currencyPair.getId()).
                        from(currencyPair.getFrom()).
                        to(currencyPair.getTo()).
                        symbol(currencyPair.getSymbol()).
                        rate(currencyPair.getRate()).
                        build());
    }
}
