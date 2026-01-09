package com.example.ExpenseTracker.Dto;

import com.example.ExpenseTracker.Entities.Type.IncomeType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResquestDto {
    private String incomeDesc;

    @NotNull(message = "Income Type cannot be null")
    private IncomeType incomeType;

    @NotNull(message = "Amount cannot be null")
    private Long incomeAmount;

    @NotNull(message = "Income date cannot be null")
    private LocalDate incomeDate;
}
