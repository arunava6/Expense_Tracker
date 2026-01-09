package com.example.ExpenseTracker.Repository;

import com.example.ExpenseTracker.Entities.Expense;
import com.example.ExpenseTracker.Entities.Type.ExpenseType;
import com.example.ExpenseTracker.Entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Long> {

//    List<Expense> findByUser(User loggedUser);
//    Optional<Expense> findByExpenseIdAndUser(Long expenseId, User loggedUser);
//
//    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.user = :user")
//    Long sumExpenseAmountByUser(@Param("user") User user);

    List<Expense> findByUser(User loggedUser);

    List<Expense> findByUser(User loggedUser, Sort sort);

    List<Expense> findByUserAndExpenseType(
            User user,
            ExpenseType expenseType,
            Sort sort
    );

    Optional<Expense> findByExpenseIdAndUser(Long expenseId, User loggedUser);

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.user = :user")
    Long sumExpenseAmountByUser(@Param("user") User user);
}
