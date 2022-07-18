package com.teamharmony.newscommunity.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends IllegalArgumentException{
    private String invalidValue;
    private String code;

    // 적절치 못한 인자를 넘겨주었을 때 //
    public InvalidRequestException(String message, String invalidValue, String code) {
        super(message);
        this.invalidValue = invalidValue;
        this.code = code;
    }

    public InvalidRequestException(String message, Throwable cause, String invalidValue, String code) {
        super(message, cause);
        this.invalidValue = invalidValue;
        this.code = code;
    }
}
