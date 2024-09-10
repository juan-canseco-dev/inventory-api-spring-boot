package com.jcanseco.inventoryapi.exceptions.handler;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";
    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @Value("${app.trace}")
    private boolean printStackTrace;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull
            MethodArgumentNotValidException ex,
            @NonNull
            HttpHeaders headers,
            @NonNull
            HttpStatusCode status,
            @NonNull
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error. Check 'errors' field for details."
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull
            Exception ex,
            Object body,
            @NonNull
            HttpHeaders headers,
            @NonNull
            HttpStatusCode statusCode,
            @NonNull
            WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.valueOf(statusCode.value()), request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        logger.error("Uncaught exception occurred", exception);
        return buildErrorResponse(exception, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request) {
        logger.error("Not found exception occurred", exception);
        return buildErrorResponse(exception, "Record Not Found", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleDomainException(DomainException exception, WebRequest request) {
        logger.error("Domain exception occurred", exception);
        return buildErrorResponse(exception, "Domain Error", HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // Security
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        logger.error("Bad credentials exception occurred", exception);
        return buildErrorResponse(exception, "Bad credentials", HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        logger.error("Access denied exception occurred", exception);
        return buildErrorResponse(exception, "Access denied", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException exception, WebRequest request) {
        logger.error("Username not found exception occurred", exception);
        return buildErrorResponse(exception, "User Not Found or Token Not Provided", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleSignatureException(SignatureException exception, WebRequest request) {
        logger.error("Signature exception occurred", exception);
        return buildErrorResponse(exception, "Signature Error", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException exception, WebRequest request) {
        logger.error("Malformed token exception occurred", exception);
        return buildErrorResponse(exception, "Malformed token", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException exception, WebRequest request) {
        logger.error("Expired token exception occurred", exception);
        return buildErrorResponse(exception, "Expired token", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException exception, WebRequest request) {
        logger.error("Unsupported jwt exception occurred", exception);
        return buildErrorResponse(exception, "Unsupported jwt", HttpStatus.UNAUTHORIZED, request);
    }
}