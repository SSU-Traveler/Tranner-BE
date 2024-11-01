package com.project.capstone.global.exception;

public enum ExceptionCode {
    // 이메일 관련
    MEMBER_EMAIL_EXISTS(409, "이미 사용중인 이메일입니다."), // 409 Conflict
    UNABLE_TO_SEND_EMAIL(500, "이메일 전송에 실패했습니다."), // 500 Internal Server Error
    INVALID_EMAIL_FORMAT(400, "유효하지 않은 이메일 형식입니다."), // 400 Bad Request
    EMAIL_NOT_REGISTERED(404, "등록되지 않은 이메일입니다."), // 404 Not Found

    // 아이디 찾기 실패시
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."), // 404 Not Found

    // 회원가입 시
    USERID_EXISTS(409, "이미 가입된 사용자입니다"), // 409 Conflict

    // 인증 코드 관련
    NO_SUCH_ALGORITHM(500, "안전한 랜덤한 값을 생성할 수 없습니다."), // 500 Internal Server Error
    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증 코드입니다."), // 400 Bad Request
    VERIFICATION_CODE_EXPIRED(400, "인증 코드가 만료되었습니다."), // 400 Bad Request

    // 로그인 관련
    FAILED_LOGIN(401, "회원정보가 일치하지 않습니다."), // 401 Unauthorized

    //jwt 토큰 관련
    FAILED_AUTHENTICATION(401,"사용자 검증에 실패하였습니다.");
    // 추가적인 예외 코드 정의 가능

    private final int status;
    private final String message; // 예외 메시지
    //생성자
    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public int getStatus() {
        return status;
    }

    // 메시지 반환
    public String getMessage() {
        return message;
    }
}
