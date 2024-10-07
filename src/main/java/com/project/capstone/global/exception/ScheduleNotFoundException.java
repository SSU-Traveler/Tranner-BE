package com.project.capstone.global.exception;

// DB에 해당 스케줄이 없을 때의 예외
public class ScheduleNotFoundException extends RuntimeException{

    public ScheduleNotFoundException() {
    }

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    public ScheduleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleNotFoundException(Throwable cause) {
        super(cause);
    }
}
