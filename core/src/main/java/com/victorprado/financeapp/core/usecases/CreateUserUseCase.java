package com.victorprado.financeapp.core.usecases;

import com.victorprado.financeapp.core.common.PasswordEncoder;
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
    log.info("validating user {}", user.getEmail());
    boolean valid = user.validate();
    if (!valid) {
      log.info("User has invalid data {}", user.getEmail());
      throw new InvalidDataException("User has invalid data");
    }
    log.info("encrypting user's password. {}", user.getEmail());
    user.setPassword(PasswordEncoder.encode(user.getPassword()));
    log.info("persisting new user {}", user.getEmail());
    return this.repository.save(user);
  }
}
