package com.teamharmony.newscommunity.aop.advice;

import com.teamharmony.newscommunity.exception.Error;
import com.teamharmony.newscommunity.exception.ErrorResponse;
import com.teamharmony.newscommunity.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = { InvalidRequestException.class })
    public ResponseEntity<Object> handleApiRequestException(InvalidRequestException ex, HttpServletRequest httpServletRequest) {
        List<Error> errorList = new ArrayList<>();

        String fieldName = "invalid Error";
        String message = ex.getMessage();
        String invalidValue = ex.getInvalidValue();


        Error errorMessage = new Error();
        errorMessage.setField(fieldName);
        errorMessage.setMessage(message);
        errorMessage.setInvalidValue(invalidValue);
        errorList.add(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage(message);
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");
        errorResponse.setCode(ex.getCode());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e){
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }
}
