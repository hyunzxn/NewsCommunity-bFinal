package com.teamharmony.newscommunity.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenException extends Exception {
    private String invalidValue;
    private String code;

		@Builder
    public TokenException(String message, Throwable cause, String invalidValue, String code) {
        super(message, cause);
        this.invalidValue = invalidValue;
        this.code = code;
    }
}
