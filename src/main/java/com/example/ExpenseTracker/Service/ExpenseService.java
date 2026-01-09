package com.example.ExpenseTracker.Service;

import com.example.ExpenseTracker.Dto.ExpenseRequestDto;
import com.example.ExpenseTracker.Entities.Expense;
import com.example.ExpenseTracker.Entities.Type.ExpenseType;
import com.example.ExpenseTracker.Entities.User;
import com.example.ExpenseTracker.Exception.ResourceNotFoundException;
import com.example.ExpenseTracker.Repository.ExpenseRepo;
import com.example.ExpenseTracker.Repository.UserRepo;
import com.example.ExpenseTracker.Security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;


    public String addExpense(@Valid ExpenseRequestDto expenseRequestDto) {
        String email=jwtUtil.getCurrenUserEmail();
        User loggedUser=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());
        Expense expense=new Expense();

        expense.setExpenseDesc(expenseRequestDto.getExpenseDesc());
        expense.setExpenseType(expenseRequestDto.getExpenseType());
        expense.setExpenseAmount(expenseRequestDto.getExpenseAmount());
        expense.setExpenseDate(expenseRequestDto.getExpenseDate());
        expense.setUser(loggedUser);

        expenseRepo.save(expense);
        return "Expense Created Successfully";
    }


//    public List<Expense> getExpense() {
//        String email=jwtUtil.getCurrenUserEmail();
//        User loggedUser=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());
//
//        return expenseRepo.findByUser(loggedUser);
//    }

    public List<Expense> getExpense(String expenseType, String sortBy, String order) {

        String email = jwtUtil.getCurrenUserEmail();
        User loggedUser = userRepo.findByEmail(email)
                .orElseThrow(ResourceNotFoundException::new);

        String sortField = (sortBy == null || sortBy.isEmpty())
                ? "expenseDate"
                : sortBy;

        Sort sort = Sort.by(
                order.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                sortField
        );

        // ✅ Filter + Sort
        if (expenseType != null && !expenseType.isBlank()) {
            ExpenseType type = ExpenseType.valueOf(expenseType.toUpperCase());
            return expenseRepo.findByUserAndExpenseType(loggedUser, type, sort);
        }

        // ✅ Only sort
        return expenseRepo.findByUser(loggedUser, sort);
    }


    public String deleteExpense(Long expenseId) {
        String email=jwtUtil.getCurrenUserEmail();
        User loggedUser=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());

        Expense expense=expenseRepo.findByExpenseIdAndUser(expenseId,loggedUser).orElseThrow(()-> new ResourceNotFoundException());
        expenseRepo.delete(expense);
        return "Expense Delete Successfully";
    }
}


