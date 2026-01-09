package com.example.ExpenseTracker.Service;

import com.example.ExpenseTracker.Dto.PnlResponseDto;
import com.example.ExpenseTracker.Entities.User;
import com.example.ExpenseTracker.Exception.ResourceNotFoundException;
import com.example.ExpenseTracker.Repository.ExpenseRepo;
import com.example.ExpenseTracker.Repository.IncomeRepo;
import com.example.ExpenseTracker.Repository.UserRepo;
import com.example.ExpenseTracker.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PnlService {
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final IncomeRepo incomeRepo;
    private final ExpenseRepo expenseRepo;

    public PnlResponseDto getPnl() {
        String email=jwtUtil.getCurrenUserEmail();
        User user=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());

        Long totalIncome=incomeRepo.sumIncomeAmountByUser(user);
        Long totalExpense = expenseRepo.sumExpenseAmountByUser(user);
        Long pnl=totalIncome-totalExpense;
        return new PnlResponseDto(totalIncome,totalExpense,pnl);
    }
}
