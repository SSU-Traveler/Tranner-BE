package com.project.capstone.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleBusinessLogicException(BusinessLogicException ex) {
        int statusCode = ex.getStatus(); // ExceptionCode에서 상태 코드 가져오기
        String message = ex.getMessage(); // ExceptionCode에서 메시지 가져오기

        return ResponseEntity
                .status(statusCode)
                .body(message);
    }
}
