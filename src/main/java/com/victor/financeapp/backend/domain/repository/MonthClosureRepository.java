package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.entity.MonthClosure;
import com.victor.financeapp.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthClosureRepository extends JpaRepository<MonthClosure, Long> {

    @Query("select m from MonthClosure m where m.user = :user and m.year = :year order by m.index desc limit 5")
    List<MonthClosure> findLastFiveByYear(User user, Integer year);

    Optional<MonthClosure> findByMonthAndYearAndUser_Email(String month, Integer year, String email);
}
