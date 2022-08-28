package com.victorprado.financeapp.core.usecases;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.CoreException;
import com.victorprado.financeapp.core.exceptions.InvalidDataException;
import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.core.validators.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateUserUseCase {

  private final UserRepository repository;

  public CreateUserUseCase(UserRepository repository) {
    this.repository = repository;
  }

  public User create(User user) throws CoreException {
    log.info("validating user {}", user);
    boolean valid = EntityValidator.validate(user);
    if (!valid) {
      log.info("User has invalid data {}", user);
      throw new InvalidDataException("User has invalid data");
    }
    return this.repository.save(user);
  }
}
