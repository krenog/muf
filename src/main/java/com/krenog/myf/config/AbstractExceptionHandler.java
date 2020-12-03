package com.krenog.myf.config;

import com.krenog.myf.dto.ErrorResponse;
import com.krenog.myf.dto.TypeOfResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  abstract class AbstractExceptionHandler extends ResponseEntityExceptionHandler {
    protected static final Map<Object, TypeOfResponse> ERROR_MAPPING = new HashMap<>();

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

        StringBuilder errors = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            errors
                    .append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("\n");
        }

        for (ObjectError globalError : globalErrors) {
            errors
                    .append(globalError.getObjectName())
                    .append(": ")
                    .append(globalError.getDefaultMessage())
                    .append("\n");
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse("not_valid_data", errors.toString());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    protected ResponseEntity<ErrorResponse> getResponse(Exception ex) {
        ErrorResponse errorResponse = null;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ERROR_MAPPING.containsKey(ex.getClass())) {
            TypeOfResponse typeOfResponse = ERROR_MAPPING.get(ex.getClass());
            errorResponse = new ErrorResponse(typeOfResponse.getCode(), typeOfResponse.getDescription());
            httpStatus = typeOfResponse.getHttpStatus();
        }
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
