package com.rateflow.currency.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document("pair")
public class CurrencyPair {
    @Id
    String id;
    String from;
    String to;
    Double rate;
    String symbol;
}
