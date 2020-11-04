package com.krenog.myf.user.controllers.exceptions;

import com.krenog.myf.dto.ErrorResponse;
import com.krenog.myf.dto.TypeOfResponse;
import com.krenog.myf.user.controllers.AuthenticationController;
import com.krenog.myf.user.controllers.UserController;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.sms.exceptions.SendSmsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(basePackageClasses = {AuthenticationController.class, UserController.class})
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LogManager.getLogger(UserExceptionHandler.class);
    private static final String UNKNOWN_CAUSE_MESSAGE = "Произошла непредвиденная ошибка.";
    private static final Map<Object, TypeOfResponse> ERROR_MAPPING = new HashMap<>();

    static {
        ERROR_MAPPING.put(SendSmsException.class, new TypeOfResponse(HttpStatus.BAD_GATEWAY, "sms_traffic_unavailable", "Ошибка отправки смс."));
        ERROR_MAPPING.put(CodeDoesNotExistException.class, new TypeOfResponse(HttpStatus.INTERNAL_SERVER_ERROR, "code_does_not_exist", "Укажите снова номер телефона и код из SMS для продолжения работы."));
        ERROR_MAPPING.put(InvalidVerificationCodeException.class, new TypeOfResponse(HttpStatus.UNAUTHORIZED, "invalid_verification_code", "Указан неверный код. Запросите новое SMS."));
        ERROR_MAPPING.put(NumberCodeAttemptsException.class, new TypeOfResponse(HttpStatus.TOO_MANY_REQUESTS, "number_code_attempts", "Превышено допустимое количество ошибок. Запросите новое SMS с кодом."));
        ERROR_MAPPING.put(UserAlreadyExistException.class, new TypeOfResponse(HttpStatus.TOO_MANY_REQUESTS, "user_already_exist", "Пользователь с таким телефоном или никнеймом уже существует"));
    }


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        boolean shouldWeLogTheException = !(ex instanceof InvalidVerificationCodeException)
                && !(ex instanceof NumberCodeAttemptsException)
                && !(ex instanceof CodeDoesNotExistException);
        if (shouldWeLogTheException) {
            log.catching(ex);
        }
        return getResponse(ex);
    }

    private ResponseEntity<ErrorResponse> getResponse(Exception ex) {
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
