package com.podo.helloprice.api.global.rest.handler;

import com.podo.helloprice.api.global.rest.ApiException;
import com.podo.helloprice.api.global.rest.response.ErrorResponse;
import com.podo.helloprice.api.global.rest.response.dto.ErrorDto;
import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleException(ApiException e) {
        final ErrorResponse response = ErrorResponse.singleError()
                .status(e.getApiStatus())
                .error(new ErrorDto(e.getField(), e.getValue(), e.getMessage()))
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBindingValidException(MethodArgumentNotValidException e) {
        final BindingResult result = e.getBindingResult();

        final List<ErrorDto> fieldErrors = result.getFieldErrors().stream()
                .map(fe -> new ErrorDto(fe.getField(), fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        final ErrorResponse response = ErrorResponse.multiError()
                .status(DefaultApiStatus.ERR_INVALID)
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity handleBindException(BindException e) {

        final BindingResult result = e.getBindingResult();
        final List<ErrorDto> fieldErrors = result.getFieldErrors().stream()
                .map(fe -> new ErrorDto(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        final ErrorResponse response = ErrorResponse.multiError()
                .status(DefaultApiStatus.ERR_INVALID)
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
