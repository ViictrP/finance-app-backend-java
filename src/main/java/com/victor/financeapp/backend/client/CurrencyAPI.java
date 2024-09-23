package com.victor.financeapp.backend.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyAPI {

    private final WebClient webClient;

    @Value("${CURRENCY.url}")
    private String currencyUrl;

    public Map<String, Object> getDollarExchangeRates(String currencyTypes) {
        return webClient.
                get()
                .uri(currencyUrl + "/" + currencyTypes)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    clientResponse -> Mono.error(new RuntimeException("Erro ao chamar API"))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
