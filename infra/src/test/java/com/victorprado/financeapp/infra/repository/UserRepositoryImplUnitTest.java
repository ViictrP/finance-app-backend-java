package com.victorprado.financeapp.infra.repository;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.infra.model.UserModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRepositoryImplUnitTest {

  final UserPostgresRepository repository = Mockito.mock(UserPostgresRepository.class);
  final UserRepositoryImpl repositoryImpl = new UserRepositoryImpl(repository);

  @Test
  void given_valid_user_when_persisting_then_return_persisted_user() {
    given(repository.save(any(UserModel.class))).willReturn(getUserModel());

    User user = User.builder().build();
    repositoryImpl.save(user);

    then(user.getId()).isNotNull();
    verify(repository).save(any(UserModel.class));
  }

  @Test
  void given_valid_user_when_persisting_then_throw_DatabaseException() {
    given(repository.save(any(UserModel.class))).willThrow(DatabaseException.class);

    User user = User.builder().build();
    thenExceptionOfType(DatabaseException.class)
      .isThrownBy(() -> repositoryImpl.save(user));

    verify(repository).save(any(UserModel.class));
  }

  public static UserModel getUserModel() {
    var user = UserModel.builder()
      .name("User")
      .build();
    user.setId("UAHSDIGIU");
    return user;
  }
}
