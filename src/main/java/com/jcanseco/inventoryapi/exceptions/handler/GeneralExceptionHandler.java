package com.jcanseco.inventoryapi.exceptions.handler;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintViolationException;;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * * Handle all exceptions and java bean validation errors
 * for all endpoints income data that use the @Valid annotation
 *
 * @author Ehab Qadah
 * https://github.com/ehabqadah/spring-boot-microservices-best-practices/blob/master/src/main/java/com/qadah/demo/controller/GeneralExceptionHandler.java
 *
 *
 */


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String ACCESS_DENIED = "Access denied!";
    public static final String RESOURCE_FORBIDDEN = "Resource Forbidden!";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n requested uri: %s";
    public static final String LIST_JOIN_DELIMITER = ",";
    public static final String FIELD_ERROR_SEPARATOR = ": ";
    private static final Logger local_logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
    private static final String ERRORS_FOR_PATH = "errors {} for path {}";
    private static final String PATH = "path";
    private static final String ERRORS = "error";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String TYPE = "type";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
                .collect(Collectors.toList());
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, validationErrors);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        return getExceptionResponseEntity(exception, HttpStatus.valueOf(status.value()), request,
                Collections.singletonList(exception.getLocalizedMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException exception, WebRequest webRequest) {
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, webRequest, Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, webRequest, Collections.singletonList(exception.getMessage()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException exception, WebRequest request) {
        final List<String> validationErrors = exception.getConstraintViolations().stream().
                map(violation ->
                        violation.getPropertyPath() + FIELD_ERROR_SEPARATOR + violation.getMessage())
                .collect(Collectors.toList());
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({DomainException.class})
    public ResponseEntity<Object> handleDomainException(
            DomainException exception, WebRequest request) {
        return getExceptionResponseEntity(exception, HttpStatus.UNPROCESSABLE_ENTITY, request, Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException exception,
            WebRequest request) {
        return getExceptionResponseEntity(exception, HttpStatus.NOT_FOUND, request, Collections.singletonList(exception.getMessage()));
    }

    /**
     * A general handler for all uncaught exceptions
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        ResponseStatus responseStatus =
                exception.getClass().getAnnotation(ResponseStatus.class);
        final HttpStatus status =
                responseStatus!=null ? responseStatus.value():HttpStatus.INTERNAL_SERVER_ERROR;
        final String localizedMessage = exception.getLocalizedMessage();
        final String path = request.getDescription(false);
        String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage:status.getReasonPhrase());
        logger.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
        return getExceptionResponseEntity(exception, status, request, Collections.singletonList(message));
    }

    /**
     * Build a detailed information about the exception in the response
     */
    private ResponseEntity<Object> getExceptionResponseEntity(final Exception exception,
                                                              final HttpStatus status,
                                                              final WebRequest request,
                                                              final List<String> errors) {
        final Map<String, Object> body = new LinkedHashMap<>();
        final String path = request.getDescription(false);
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, status.value());
        body.put(ERRORS, errors);
        body.put(TYPE, exception.getClass().getSimpleName());
        body.put(PATH, path);
        body.put(MESSAGE, getMessageForStatus(status));
        final String errorsMessage = CollectionUtils.isNotEmpty(errors) ?
                errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(LIST_JOIN_DELIMITER))
                :status.getReasonPhrase();
        local_logger.error(ERRORS_FOR_PATH, errorsMessage, path);
        return new ResponseEntity<>(body, status);
    }

    private String getMessageForStatus(HttpStatus status) {
        return switch (status) {
            case UNAUTHORIZED -> ACCESS_DENIED;
            case FORBIDDEN -> RESOURCE_FORBIDDEN;
            case BAD_REQUEST -> INVALID_REQUEST;
            default -> status.getReasonPhrase();
        };
    }
}