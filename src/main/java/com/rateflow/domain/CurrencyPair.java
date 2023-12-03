package com.rateflow.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Document("pair")
public class CurrencyPair {
    @Id
    String id;
    @NotEmpty
    String from;
    @NotEmpty
    String to;
    @NotNull
    Double rate;
    String symbol;
}
