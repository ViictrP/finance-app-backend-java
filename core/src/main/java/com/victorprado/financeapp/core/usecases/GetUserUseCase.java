package com.victorprado.financeapp.core.usecases;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.CoreException;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.core.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetUserUseCase {

  private final UserRepository repository;

  public GetUserUseCase(UserRepository repository) {
    this.repository = repository;
  }

  public User get(String userId) {
    try {
      log.info("getting user by id {}", userId);
      User user = this.repository.getById(userId);
      log.info("user {} {} found!", user.getName(), user.getLastname());
      return user;
    } catch (DatabaseException exception) {
      log.error(exception.getMessage());
      throw new CoreException(exception.getMessage());
    }
  }
}
