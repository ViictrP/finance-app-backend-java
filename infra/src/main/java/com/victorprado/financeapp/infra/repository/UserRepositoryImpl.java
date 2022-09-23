package com.victorprado.financeapp.infra.repository;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.core.repositories.UserRepository;
import com.victorprado.financeapp.infra.mapper.UserEntityModelMapper;
import com.victorprado.financeapp.infra.model.UserModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

  private final UserPostgresRepository repository;
  private final UserEntityModelMapper mapper;

  public UserRepositoryImpl(UserPostgresRepository repository, UserEntityModelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public User save(User user) throws DatabaseException {
    try {
      log.info("transforming new user {}", user.getEmail());
      UserModel userModel = mapper.toModel(user);
      userModel.setActive(true);
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

  @Override
  public User getById(String id) throws DatabaseException {
    try {
      log.info("fetching user {} data", id);
      UserModel userModel = repository.findById(id)
        .orElseThrow(() -> new DatabaseException("user not found for ID " + id));
      return mapper.toEntity(userModel);
    } catch (Exception exception) {
      log.info("An error occured while getting user {}", id);
      throw new DatabaseException(exception.getMessage());
    }
  }
}
