package com.victorprado.financeapp.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors().and()
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/v1/**").authenticated()
      .and()
      .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/finance-app-oidc"))
      .oauth2Client(withDefaults());
    return http.build();
  }
}
