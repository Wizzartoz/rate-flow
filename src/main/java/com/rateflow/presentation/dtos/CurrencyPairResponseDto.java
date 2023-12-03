package com.rateflow.presentation.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
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
