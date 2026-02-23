package com.blogapplication.Exception;

import com.blogapplication.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> responseNotfoundExceptionHandler(ResourceNotFoundException ex){
        String msg=ex.getMessage();
        ApiResponse apiResponse=new ApiResponse(msg,false,MDC.get("requestId"));
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodAgrNotValidException(MethodArgumentNotValidException ex){
        Map<String,Object> res = new HashMap<>();
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            errors.put(fieldName, err.getDefaultMessage());
        });

        res.put("status", 400);
        res.put("errors", errors);
        res.put("requestId", MDC.get("requestId"));

        return ResponseEntity.badRequest().body(res);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logger.warn("UserAlreadyExistsException: {}", ex.getMessage());
        String requestId= MDC.get("requestId");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "message", ex.getMessage(),
                        "status", 409,
                        "requestId", requestId,
                        "timestamp", java.time.Instant.now().toString()
                )
        );
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "message", "User already exists (duplicate email).",
                        "status", 409,
                        "requestId", MDC.get("requestId"),
                        "timestamp", java.time.Instant.now().toString()
                )
        );
    }




}
