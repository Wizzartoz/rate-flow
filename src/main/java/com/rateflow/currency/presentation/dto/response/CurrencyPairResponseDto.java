package com.rateflow.currency.presentation.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyPairResponseDto {
    @NotEmpty
    String id;
    @NotEmpty
    String from;
    @NotEmpty
    String to;
    @NotNull
    Double rate;
    String symbol;
}
