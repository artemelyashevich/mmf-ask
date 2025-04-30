package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.exception.ExceptionBody;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.mongodb.MongoWriteException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestControllerAdvice
public class ControllerAdvice {

    private static final String NOT_FOUND_MESSAGE = "Resource was not found.";
    private static final String ALREADY_EXISTS_MESSAGE = "Resource already exists.";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid access token.";
    private static final String INVALID_PASSWORD_MESSAGE = "Password mismatch.";
    private static final String FAILED_VALIDATION_MESSAGE = "Validation failed.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Something went wrong.";
    private static final String DUPLICATE_KEY_EXCEPTION = "Item with such data already exists.";
    private static final String NOT_SUPPORTED_MESSAGE = "Http method with this URL not found.";
    private static final String TOKEN_EXPIRED_MESSAGE = "JWT expired.";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied.";

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleNotFound(final ResourceNotFoundException exception) {
        var message = exception.getMessage() == null ? NOT_FOUND_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", NOT_FOUND_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleAlreadyExists(final ResourceAlreadyExistsException exception) {
        var message = exception.getMessage() == null ? ALREADY_EXISTS_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", ALREADY_EXISTS_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @SuppressWarnings("all")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidation(final MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                fieldError -> fieldError.getDefaultMessage(),
                                (exist, newMessage) -> exist + " " + newMessage
                        )
                );
        return new ExceptionBody(FAILED_VALIDATION_MESSAGE, errors);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleDuplicateKey(final MongoWriteException exception) {
        var message = exception.getMessage() == null ? DUPLICATE_KEY_EXCEPTION : exception.getMessage();
        log.warn("{} '{}'.", DUPLICATE_KEY_EXCEPTION, message);
        return new ExceptionBody(message);
    }


    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionBody handleInvalidToken(final InvalidTokenException exception) {
        var message = exception.getMessage() == null ? INVALID_TOKEN_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", INVALID_TOKEN_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleInvalidPassword(final InvalidPasswordException exception) {
        var message = exception.getMessage() == null ? INVALID_PASSWORD_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", INVALID_PASSWORD_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleException(final HttpRequestMethodNotSupportedException exception) {
        var message = exception.getMessage() == null ? NOT_SUPPORTED_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", NOT_SUPPORTED_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionBody handleTokenExpire(final ExpiredJwtException exception) {
        var message = exception.getMessage() == null ? TOKEN_EXPIRED_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", TOKEN_EXPIRED_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleDeniedException(final AuthorizationDeniedException exception) {
        var message = exception.getMessage() == null ? ACCESS_DENIED_MESSAGE : exception.getMessage();
        log.warn("{} '{}'.", ACCESS_DENIED_MESSAGE, message);
        return new ExceptionBody(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(final RuntimeException exception) {
        exception.printStackTrace();
        log.error(exception.getMessage(), exception.getCause());
        return new ExceptionBody(UNEXPECTED_ERROR_MESSAGE);
    }
}