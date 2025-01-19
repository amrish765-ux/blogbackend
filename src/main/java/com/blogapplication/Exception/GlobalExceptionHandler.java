package com.blogapplication.Exception;

import com.blogapplication.payload.ApiResponse;
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
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> responseNotfoundExceptionHandler(ResourceNotFoundException ex){
        String msg=ex.getMessage();
        ApiResponse apiResponse=new ApiResponse(msg,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleMethodAgrNotValidException(MethodArgumentNotValidException ex){
        Map<String,String>res=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((err)->{
            String fieldName=((FieldError)err).getField();
            String message=err.getDefaultMessage();
            res.put(fieldName,message);
        });
        return new ResponseEntity<Map<String,String>>(res,HttpStatus.BAD_REQUEST);
    }
}
