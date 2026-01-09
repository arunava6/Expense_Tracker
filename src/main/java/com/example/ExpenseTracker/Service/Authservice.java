package com.example.ExpenseTracker.Service;

import com.example.ExpenseTracker.Dto.LoginRequestDto;
import com.example.ExpenseTracker.Dto.LoginResponseDto;
import com.example.ExpenseTracker.Dto.SignupRequestDto;
import com.example.ExpenseTracker.Entities.Type.ProviderType;
import com.example.ExpenseTracker.Entities.User;
import com.example.ExpenseTracker.Exception.ResourceNotFoundException;
import com.example.ExpenseTracker.Exception.UserAlreadyExistsException;
import com.example.ExpenseTracker.Repository.UserRepo;
import com.example.ExpenseTracker.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Authservice {
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
//        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
//        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow();
//        String token = jwtUtil.generateToken(user.getEmail());
//        return new LoginResponseDto(token);

        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException());

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDto(token);
    }


    public void signup(SignupRequestDto signupRequestDto) {

        userRepo.findByEmail(signupRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        User user = new User();
        user.setName(signupRequestDto.getName());
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setProviderType(ProviderType.LOCAL);
        user.setProviderId(null);

        userRepo.save(user);
    }
}


