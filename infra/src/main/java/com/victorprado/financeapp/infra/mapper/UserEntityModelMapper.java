package com.victorprado.financeapp.infra.mapper;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.infra.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityModelMapper extends EntityModelMapper<User, UserModel> {

}
