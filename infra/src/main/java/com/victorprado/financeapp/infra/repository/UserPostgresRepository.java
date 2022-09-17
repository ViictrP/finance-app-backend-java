package com.victorprado.financeapp.infra.repository;

import com.victorprado.financeapp.infra.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostgresRepository extends JpaRepository<UserModel, String> {

}
