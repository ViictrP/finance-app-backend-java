package com.victorprado.financeapp.infra.repository;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.infra.mapper.UserEntityModelMapper;
import com.victorprado.financeapp.infra.model.UserModel;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRepositoryImplUnitTest {

  final UserPostgresRepository repository = Mockito.mock(UserPostgresRepository.class);
  final UserEntityModelMapper mapper = Mockito.mock(UserEntityModelMapper.class);
  final UserRepositoryImpl repositoryImpl = new UserRepositoryImpl(repository, mapper);

  @Test
  void given_valid_user_when_persisting_then_return_persisted_user() {
    given(repository.save(any(UserModel.class))).willReturn(getUserModel());
    given(mapper.toModel(any(User.class))).willReturn(getUserModel());

    User user = User.builder().build();
    repositoryImpl.save(user);

    then(user.getId()).isNotNull();
    verify(repository).save(any(UserModel.class));
  }

  @Test
  void given_valid_user_when_persisting_then_throw_DatabaseException() {
    given(repository.save(any(UserModel.class))).willThrow(DatabaseException.class);
    given(mapper.toModel(any(User.class))).willReturn(getUserModel());

    User user = User.builder().build();
    thenExceptionOfType(DatabaseException.class)
      .isThrownBy(() -> repositoryImpl.save(user));

    verify(repository).save(any(UserModel.class));
  }

  @Test
  void given_a_id_when_getting_user_then_should_return_user() {
    given(repository.findById(anyString())).willReturn(Optional.of(getUserModel()));
    given(mapper.toEntity(any(UserModel.class))).willReturn(getUser());

    User user = repositoryImpl.getById("test");

    then(user).isNotNull();

    verify(repository).findById(anyString());
  }

  @Test
  void given_a_id_when_getting_user_then_should_throwException() {
    given(repository.findById(anyString())).willThrow(DatabaseException.class);

    thenExceptionOfType(DatabaseException.class)
      .isThrownBy(() -> repositoryImpl.getById("test"));

    verify(repository).findById(anyString());
  }

  public UserModel getUserModel() {
    var user = UserModel.builder()
      .name("User")
      .build();
    user.setId(1L);
    return user;
  }

  public User getUser() {
    return User.builder().build();
  }
}
