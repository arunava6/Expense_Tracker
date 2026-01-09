package com.example.ExpenseTracker.Controller;

import com.example.ExpenseTracker.Dto.IncomeResquestDto;
import com.example.ExpenseTracker.Entities.Income;
import com.example.ExpenseTracker.Service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/income")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/add")
    public ResponseEntity<String> addIncome(@Valid @RequestBody IncomeResquestDto incomeResquestDto){
        incomeService.addIncome(incomeResquestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Income added Successfully");
    }

//    @GetMapping("/get")
//    public ResponseEntity<List<Income>> getIncome(){
//        return ResponseEntity.status(HttpStatus.FOUND).body(incomeService.getIncome());
//    }

    @GetMapping("/get")
    public ResponseEntity<List<Income>> getIncome(
            @RequestParam(required = false) String incomeType,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(
                incomeService.getIncome(incomeType, sortBy, order)
        );
    }


    @DeleteMapping("/delete/{incomeId}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long incomeId){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.deleteIncome(incomeId));
    }
}

