package com.example.ExpenseTracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PnlResponseDto {
    private Long totalIncomeAmount;
    private Long totalExpenseAmount;
    private Long totalPnl;
}
