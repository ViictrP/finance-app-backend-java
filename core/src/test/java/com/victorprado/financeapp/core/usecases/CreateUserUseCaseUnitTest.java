package com.victorprado.financeapp.core.usecases;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.CoreException;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import com.victorprado.financeapp.core.exceptions.InvalidDataException;
import com.victorprado.financeapp.core.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseUnitTest {

  final UserRepository repository = Mockito.mock(UserRepository.class);
  final CreateUserUseCase useCase = new CreateUserUseCase(repository);

  @Test
  void given_valid_user_then_should_create_user_with_success() {
    given(repository.save(any(User.class))).willReturn(getUser());
    User user = useCase.create(getUser());
    then(user).usingRecursiveComparison().isEqualTo(getUser());
    verify(repository).save(any(User.class));
  }

  @Test
  void given_invalid_user_then_should_throw_exception() {
    User invalidUser = getUser();
    invalidUser.setName(null);
    thenExceptionOfType(InvalidDataException.class)
      .isThrownBy(() -> useCase.create(invalidUser))
      .withMessage("User has invalid data");
  }

  @Test
  void given_existing_user_then_should_throw_database_exception() {
    given(repository.save(any(User.class)))
      .willThrow(new DatabaseException("User already exists"));

    thenExceptionOfType(CoreException.class)
      .isThrownBy(() -> useCase.create(getUser()))
      .withMessage("User already exists");
  }

  private User getUser() {
    return User.builder()
      .name("Victor")
      .lastName("Prado")
      .email("a@a.com")
      .password("1234")
      .build();
  }
}
