package com.example.ExpenseTracker.Controller;

import com.example.ExpenseTracker.Dto.ExpenseRequestDto;
import com.example.ExpenseTracker.Entities.Expense;
import com.example.ExpenseTracker.Service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(expenseRequestDto));
    }

//    @GetMapping("/get")
//    public ResponseEntity<List<Expense>> getExpense(){
//        return ResponseEntity.status(HttpStatus.FOUND).body(expenseService.getExpense());
//    }

    @GetMapping("/get")
    public ResponseEntity<List<Expense>> getExpense(
            @RequestParam(required = false) String expenseType,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(
                expenseService.getExpense(expenseType, sortBy, order)
        );
    }


    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.deleteExpense(expenseId));
    }
}
