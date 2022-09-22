package com.victorprado.financeapp.entrypoint.mapper;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.entrypoint.request.UserRequest;
import com.victorprado.financeapp.entrypoint.response.UserResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
  componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = {
    CreditCardEntityResponseMapper.class,
    TransactionEntityResponseMapper.class
  })
public interface UserEntityResponseMapper extends EntityResponseMapper<User, UserResponse, UserRequest> {

}
