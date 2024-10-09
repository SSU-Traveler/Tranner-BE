package com.project.capstone.global.exception;

public class BusinessLogicException extends RuntimeException {
    private final ExceptionCode code; // 예외 코드

    // 생성자
    public BusinessLogicException(ExceptionCode code) {
        super(code.getMessage()); // 상위 클래스의 메시지에 예외 코드의 메시지를 전달
        this.code = code;
    }

    // 코드 반환
    public ExceptionCode getCode() {
        return code;
    }
}
