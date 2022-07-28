package com.teamharmony.newscommunity.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
public class Error {
    private String field;
    private String code;
    private String message;
    private String invalidValue;

    @Builder
    public Error(String field, String code, String message, String invalidValue) {
        this.field = field;
        this.code = code;
        this.message = message;
        this.invalidValue = invalidValue;
    }
}
