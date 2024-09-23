package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = """
            select u.*
            from finance_app.finance_app_user u
                     inner join finance_app.user_property up on u.id = up.user_id
            where up.property_name = 'MONTH_CLOSURE_DAY'
              and up.property_value = :dayOfTheMonth
              and u.id not in (select user_id from finance_app.month_closure m where m.month = :month and m.year = :year and m.deleted is false)""", nativeQuery = true)
    List<User> findAllUsersWithMonthClosureTodayAndWithoutMonthClosuresOnMonthAndYear(@Param("dayOfTheMonth") String dayOfTheMonth,
                                                 @Param("month") String month,
                                                 @Param("year") Integer year);
}
