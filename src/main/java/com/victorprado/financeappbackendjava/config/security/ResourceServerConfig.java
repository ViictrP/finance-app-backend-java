package com.victorprado.financeappbackendjava.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ResourceServerConfig {

  private final KeycloakJwtAuthenticationConverter converter;

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
      .oauth2ResourceServer()
      .jwt().jwtAuthenticationConverter(converter);
    return http.build();
  }
}
