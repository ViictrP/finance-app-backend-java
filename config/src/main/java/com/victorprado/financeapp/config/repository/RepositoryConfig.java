package com.victorprado.financeapp.config.repository;

import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.infra.repository.UserPostgresRepository;
import com.victorprado.financeapp.infra.repository.UserPostgresRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

  private final UserPostgresRepository userPostgresRepository;

  public RepositoryConfig(UserPostgresRepository userPostgresRepository) {
    this.userPostgresRepository = userPostgresRepository;
  }

  @Bean
  public UserRepository userRepository() {
    return new UserPostgresRepositoryImpl(userPostgresRepository);
  }
}
