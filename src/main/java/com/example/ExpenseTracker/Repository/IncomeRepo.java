package com.example.ExpenseTracker.Repository;

import com.example.ExpenseTracker.Entities.Income;
import com.example.ExpenseTracker.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepo extends JpaRepository<Income,Long> {
    List<Income> findByUser(User user);
    Optional<Income> findByIncomeIdAndUser(Long incomeId,User user);
}
