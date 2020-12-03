package com.krenog.myf.event.controller.exceptions;

import com.krenog.myf.config.AbstractExceptionHandler;
import com.krenog.myf.dto.ErrorResponse;
import com.krenog.myf.dto.TypeOfResponse;
import com.krenog.myf.event.controller.EventController;
import com.krenog.myf.event.controller.InviteController;
import com.krenog.myf.event.exceptions.UserAlreadyInvitedException;
import com.krenog.myf.event.services.member.exceptions.MemberAlreadyExistException;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {EventController.class, InviteController.class})
public class EventExceptionHandler extends AbstractExceptionHandler {
    private static final Logger log = LogManager.getLogger(EventExceptionHandler.class);
    private static final String UNKNOWN_CAUSE_MESSAGE = "Произошла непредвиденная ошибка.";
    static {
        ERROR_MAPPING.put(NotFoundException.class, new TypeOfResponse(HttpStatus.NOT_FOUND, "not_found", "Данные не найдены"));
        ERROR_MAPPING.put(UserAlreadyInvitedException.class, new TypeOfResponse(HttpStatus.BAD_REQUEST, "user_already_invited", "Пользователь уже приглашен"));
        ERROR_MAPPING.put(MemberAlreadyExistException.class, new TypeOfResponse(HttpStatus.BAD_REQUEST, "user_already_member", "Пользователь уже является участником"));
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
}
