package com.victorprado.financeapp.infra.repository;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.infra.model.UserModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

  private final UserPostgresRepository repository;

  public UserRepositoryImpl(UserPostgresRepository repository) {
    this.repository = repository;
  }

  @Override
  public User save(User user) throws DatabaseException {
    try {
      log.info("transforming new user {}", user.getEmail());
      UserModel userModel = new UserModel(user);
      log.info("persisting new user {} into database", user.getEmail());
      UserModel savedUser = repository.save(userModel);
      log.info("new user {} created", userModel.getId());
      user.setId(savedUser.getId());
      return user;
    } catch (Exception exception) {
      log.info("An error occured while saving new user {}", user.getEmail());
      throw new DatabaseException(exception.getMessage());
    }
  }
}
