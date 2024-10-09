package com.project.capstone.global.exception;

public enum ExceptionCode {
    UNABLE_TO_SEND_EMAIL("이메일 전송에 실패했습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    USERID_EXISTS("이미 가입된 사용자입니다"),
    MEMBER_EMAIL_EXISTS("이미 사용중인 이메일입니다."),
    NO_SUCH_ALGORITHM("안전한 랜덤한 값을 생성할 수 없습니다."),
    INVALID_VERIFICATION_CODE("유효하지 않은 인증 코드입니다."),
    VERIFICATION_CODE_EXPIRED("인증 코드가 만료되었습니다."),
    FAILED_LOGIN("회원정보가 일치하지 않습니다.");

    // 추가적인 예외 코드 정의 가능

    private final String message; // 예외 메시지

    // 생성자
    ExceptionCode(String message) {
        this.message = message;
    }

    // 메시지 반환
    public String getMessage() {
        return message;
    }
}
