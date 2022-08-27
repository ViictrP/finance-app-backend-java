package com.victorprado.financeapp.core.usecases;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateUserUseCase {

  private final UserRepository repository;

  public CreateUserUseCase(UserRepository repository) {
    this.repository = repository;
  }

  public User create(User user) {
    return user;
  }
}
