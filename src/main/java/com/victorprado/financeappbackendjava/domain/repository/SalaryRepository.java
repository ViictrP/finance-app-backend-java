package com.victorprado.financeappbackendjava.domain.repository;

import com.victorprado.financeappbackendjava.domain.entity.Salary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
  Optional<Salary> findByUserIdAndMonthAndYear(String userId, String month, Integer year);
}
