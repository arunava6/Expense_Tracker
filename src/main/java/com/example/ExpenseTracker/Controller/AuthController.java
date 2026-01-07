package com.example.ExpenseTracker.Controller;

import com.example.ExpenseTracker.Dto.LoginRequestDto;
import com.example.ExpenseTracker.Dto.LoginResponseDto;
import com.example.ExpenseTracker.Dto.SignupRequestDto;
import com.example.ExpenseTracker.Service.Authservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Authservice authservice;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authservice.login(loginRequestDto));

    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        authservice.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully!!");

    }

}
