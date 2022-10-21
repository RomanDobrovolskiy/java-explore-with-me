package ru.practicum.explore_with_me.handler;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.explore_with_me.exception.*;
import ru.practicum.explore_with_me.utils.ApiError;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setReason("Validation error");
        apiError.setErrors(e.getConstraintViolations().stream()
                .map(cv -> "Object: " + cv.getLeafBean().toString() +
                        ". Field: " + ((PathImpl) cv.getPropertyPath()).getLeafNode().asString() +
                        ". Value: " + (cv.getInvalidValue() != null ? cv.getInvalidValue().toString() : "null") +
                        ". Message: " + cv.getMessage())
                .collect(Collectors.toList()));
        apiError.setMessage("");
        return createResponse(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Malformed JSON request");
        apiError.setReason(ex.getCause() != null ? ex.getCause().getMessage() : null);
        return createResponse(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setErrors(ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "Field: " + error.getField() + ". Error: " + error.getDefaultMessage() +
                        ". Value: " + error.getRejectedValue())
                .collect(Collectors.toList()));
        apiError.setMessage("During [" + ex.getBindingResult().getObjectName() + "] validation " +
                ex.getBindingResult().getFieldErrors().size() + " errors were found");
        apiError.setReason("Incorrectly made request.");
        return createResponse(apiError);
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, EventNotFoundException.class,
            RequestNotFoundException.class, CompilationNotFoundException.class, LocationNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setReason("The required object was not found.");
        apiError.setMessage(e.getMessage());
        return createResponse(apiError);
    }

    @ExceptionHandler({EventDateTooEarlyException.class, EventBadRequestException.class, LocationBadRequest.class})
    public ResponseEntity<Object> handleEventExceptions(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Database error");
            apiError.setErrors(List.of(ex.getCause().getCause().getMessage()));
            return createResponse(apiError);
        }
        return createResponse(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    private ResponseEntity<Object> createResponse(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
