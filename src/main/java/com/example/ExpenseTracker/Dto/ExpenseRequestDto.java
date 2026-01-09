package com.example.ExpenseTracker.Dto;

import com.example.ExpenseTracker.Entities.Type.ExpenseType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequestDto {
    private String expenseDesc;

    @NotNull(message = "Type should not be null")
    private ExpenseType expenseType;

    @NotNull
    @Positive
    private Long expenseAmount;

    @NotNull
    private LocalDate expenseDate;

}
