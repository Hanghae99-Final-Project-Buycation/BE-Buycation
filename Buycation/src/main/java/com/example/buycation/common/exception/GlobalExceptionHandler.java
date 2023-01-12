package com.example.buycation.common.exception;

import com.example.buycation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.buycation.common.exception.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<?> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(new ResponseMessage<>(ex.getErrorCode().getMsg(), ex.getErrorCode().getStatusCode(), ex.getErrorCode())
                , HttpStatus.OK);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        for (FieldError error : result.getFieldErrors()) {
                    if(error.getField().equals("email")) {
                        return new ResponseEntity<>(new ResponseMessage<>(INVALID_EMAIL_PATTERN, INVALID_EMAIL_PATTERN), HttpStatus.NOT_FOUND);
                    }else if (error.getField().equals("password")) {
                        return new ResponseEntity<>(new ResponseMessage<>(INVALID_PASSWORD_PATTERN, INVALID_PASSWORD_PATTERN), HttpStatus.NOT_FOUND);
                    }else {
                        return new ResponseEntity<>(new ResponseMessage<>(INVALID_NICKNAME_PATTERN, INVALID_NICKNAME_PATTERN), HttpStatus.NOT_FOUND);
                    }
        }
        return new ResponseEntity<>(new ResponseMessage<>(INVALID_PARAMETER, INVALID_PARAMETER), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})

    protected ResponseEntity<?> handleServerException(Exception ex) {
        return new ResponseEntity<>(new ResponseMessage<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "error")
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}