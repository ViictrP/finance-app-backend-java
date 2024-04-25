package com.victorprado.financeappbackendjava.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "CURRENCY")
public interface CurrencyAPI {

    @GetMapping("/{currencyTypes}")
    ResponseEntity<Map<String, Object>> getDollarExchangeRates(@PathVariable String currencyTypes);
}
