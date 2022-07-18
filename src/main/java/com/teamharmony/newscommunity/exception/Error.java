package com.teamharmony.newscommunity.exception;

import lombok.Data;
import lombok.Setter;

@Data
public class Error {
    private String field;
    private String message;
    private String invalidValue;
}
