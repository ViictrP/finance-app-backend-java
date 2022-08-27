package com.victorprado.financeapp.core.usecases;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
  void test() {
    given(repository.save(any(User.class))).willReturn(getUser());

    User user = useCase.create(new User());

    then(user).usingRecursiveComparison().isEqualTo(getUser());
  }

  private User getUser() {
    return new User();
  }
}
