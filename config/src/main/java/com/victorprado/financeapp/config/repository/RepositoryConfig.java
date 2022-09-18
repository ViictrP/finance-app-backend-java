package com.victorprado.financeapp.config.repository;

import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.infra.mapper.UserEntityModelMapper;
import com.victorprado.financeapp.infra.repository.UserPostgresRepository;
import com.victorprado.financeapp.infra.repository.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

  private final UserPostgresRepository userPostgresRepository;
  private final UserEntityModelMapper userEntityModelMapper;

  public RepositoryConfig(
    UserPostgresRepository userPostgresRepository,
    UserEntityModelMapper userEntityModelMapper) {
    this.userPostgresRepository = userPostgresRepository;
    this.userEntityModelMapper = userEntityModelMapper;
  }

  @Bean
  public UserRepository userRepository() {
    return new UserRepositoryImpl(userPostgresRepository, userEntityModelMapper);
  }
}
