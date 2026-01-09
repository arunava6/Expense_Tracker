package com.example.ExpenseTracker.Repository;

import com.example.ExpenseTracker.Entities.Income;
import com.example.ExpenseTracker.Entities.Type.IncomeType;
import com.example.ExpenseTracker.Entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepo extends JpaRepository<Income,Long> {
//    List<Income> findByUser(User user);
//    Optional<Income> findByIncomeIdAndUser(Long incomeId,User user);
//
//    @Query("SELECT COALESCE(SUM(i.incomeAmount), 0) FROM Income i WHERE i.user = :user")
//    Long sumIncomeAmountByUser(@Param("user") User user);


    List<Income> findByUser(User user);

    List<Income> findByUser(User user, Sort sort);

    List<Income> findByUserAndIncomeType(User user, IncomeType incomeType, Sort sort);

    Optional<Income> findByIncomeIdAndUser(Long incomeId, User user);

    @Query("SELECT COALESCE(SUM(i.incomeAmount), 0) FROM Income i WHERE i.user = :user")
    Long sumIncomeAmountByUser(@Param("user") User user);

}
