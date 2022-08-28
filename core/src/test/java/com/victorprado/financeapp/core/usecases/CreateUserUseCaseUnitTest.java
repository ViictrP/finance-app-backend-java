package com.victorprado.financeapp.core.usecases;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.victorprado.financeapp.core.entities.User;
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
  void should_create_new_user_with_success() {
    given(repository.save(any(User.class))).willReturn(getUser());
    User user = useCase.create(new User());
    then(user).usingRecursiveComparison().isEqualTo(getUser());
    verify(repository).save(any(User.class));
  }

  private User getUser() {
    return new User();
  }
}
