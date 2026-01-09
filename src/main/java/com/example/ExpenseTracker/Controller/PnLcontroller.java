package com.example.ExpenseTracker.Controller;

import com.example.ExpenseTracker.Dto.PnlResponseDto;
import com.example.ExpenseTracker.Service.PnlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class PnLcontroller {
    private final PnlService pnLService;

    @GetMapping("/pnl")
    public ResponseEntity<PnlResponseDto> getPnl(){
        return ResponseEntity.status(HttpStatus.OK).body(pnLService.getPnl());
    }
}
