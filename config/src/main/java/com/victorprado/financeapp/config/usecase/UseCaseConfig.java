package com.victorprado.financeapp.config.usecase;

import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.core.usecases.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  private final UserRepository userRepository;

  public UseCaseConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  public CreateUserUseCase createUserUseCase() {
    return new CreateUserUseCase(userRepository);
  }
}
