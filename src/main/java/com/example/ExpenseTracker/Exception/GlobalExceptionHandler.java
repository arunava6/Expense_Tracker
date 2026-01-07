package com.example.ExpenseTracker.Exception;

import com.example.ExpenseTracker.Dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
//
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(ErrorResponse.builder()
//                        .error(HttpStatus.CONFLICT.name())
//                        .message(ex.getMessage())
//                        .timestamp(LocalDateTime.now())
//                        .build());
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ErrorResponse.builder()
//                        .error(HttpStatus.NOT_FOUND.name())
//                        .message(ex.getMessage())
//                        .timestamp(LocalDateTime.now())
//                        .build());
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(ErrorResponse.builder()
//                        .error(HttpStatus.BAD_REQUEST.name())
//                        .message(ex.getMessage())
//                        .timestamp(LocalDateTime.now())
//                        .build());
//    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.NOT_FOUND.name())
                        .message("Resource not found")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists() {
        log.warn("User already exists");

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.CONFLICT.name())
                        .message("User Already Registered!!")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /* ===================== VALIDATION ===================== */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.name())
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.name())
                        .message("Invalid request")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /* ===================== SECURITY ===================== */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.UNAUTHORIZED.name())
                        .message("Bad credentials")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.NOT_FOUND.name())
                        .message("User not found")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.FORBIDDEN.name())
                        .message("Access denied")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /* ===================== FALLBACK ===================== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {
        log.error("Unexpected error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .message("Something went wrong")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}
