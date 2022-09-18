package com.victorprado.financeapp.core.repositories;

import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.core.exceptions.DatabaseException;

public interface UserRepository {
  User save(User user) throws DatabaseException;
  User getById(String id) throws DatabaseException;
}
