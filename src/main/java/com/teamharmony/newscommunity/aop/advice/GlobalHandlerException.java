package com.teamharmony.newscommunity.aop.advice;

import com.teamharmony.newscommunity.exception.*;
import com.teamharmony.newscommunity.exception.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.teamharmony.newscommunity.domain.auth.util.CookieUtil.removeRefCookie;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler(value = { InvalidRequestException.class })
    public ResponseEntity<Object> handleApiRequestException(InvalidRequestException ex, HttpServletRequest httpServletRequest) {
        List<Error> errorList = new ArrayList<>();

        String fieldName = "invalid Error";
        String message = ex.getMessage();
        String invalidValue = ex.getInvalidValue();
        String code = ex.getCode();

        Error errorMessage = Error.builder()
                                  .field(fieldName)
                                  .code(code)
                                  .message(message)
                                  .invalidValue(invalidValue)
                                  .build();
        errorList.add(errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorList(errorList)
                                                   .requestUrl(httpServletRequest.getRequestURI())
                                                   .statusCode(HttpStatus.BAD_REQUEST.toString())
                                                   .resultCode("FAIL")
                                                   .build();

        log.error("Oops so sick, harmony: {}", message , ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(value = { AuthException.class })
    public ResponseEntity<Object> handleApiRequestException(AuthException ex, HttpServletRequest httpServletRequest) {
        List<Error> errorList = new ArrayList<>();

        String fieldName = "Token Refresh Error";
        String message = ex.getMessage();
        String invalidValue = ex.getInvalidValue();
        String code = ex.getCode();

        Error errorMessage = Error.builder()
                                  .field(fieldName)
                                  .code(code)
                                  .message(message)
                                  .invalidValue(invalidValue)
                                  .build();
        errorList.add(errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorList(errorList)
                                                   .requestUrl(httpServletRequest.getRequestURI())
                                                   .statusCode(HttpStatus.BAD_REQUEST.toString())
                                                   .resultCode("FAIL")
                                                   .build();

        log.error("Oops so sick, harmony: {}", message , ex);
        return ResponseEntity.badRequest().header(SET_COOKIE, removeRefCookie()).body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exception(Exception ex){
        System.out.println(ex.getClass().getName());
        log.error("Oops so sick, harmony: {}", ex.getClass().getName() , ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
    }
}
