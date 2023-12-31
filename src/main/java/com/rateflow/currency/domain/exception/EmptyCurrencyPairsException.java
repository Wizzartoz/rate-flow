package com.rateflow.currency.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Currency pairs not found")
public class EmptyCurrencyPairsException extends RuntimeException {}
