package com.trilogyed.retailapiservice.controller;

import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.trilogyed.retailapiservice.exception.*;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> validationError(MethodArgumentNotValidException e, WebRequest request) {
        // BindingResult holds the validation errors
        BindingResult result = e.getBindingResult();
        // Validation errors are stored in FieldError objects
        List<FieldError> fieldErrors = result.getFieldErrors();

        // Translate the FieldErrors to VndErrors
        List<VndErrors.VndError> vndErrorList = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            VndErrors.VndError vndError = new VndErrors.VndError(request.toString(),
                    fieldError.getField() + ": " + fieldError.getDefaultMessage());
            vndErrorList.add(vndError);
        }

        // Wrap all of the VndError objects in a VndErrors object
        VndErrors vndErrors = new VndErrors(vndErrorList);

        // Create and return the ResponseEntity
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(vndErrors, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> outOfRangeException(IllegalArgumentException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {NumberFormatException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> numberFormatException(NumberFormatException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> notFoundException(NoSuchElementException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "Not found : " + e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(value = {ClientException.class, ServiceUnavailableException.class, HystrixBadRequestException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<VndErrors> clientException(Throwable e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "Unable to process your request. " +
                "One of the internal services is down a the moment.");
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        return responseEntity;
    }

    @ExceptionHandler(value = {FuturesException.class})
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ResponseEntity<VndErrors> futuresException(FuturesException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "An internal service was interrupt while " +
                "executing your request. Please try your request again.");
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.GATEWAY_TIMEOUT);
        return responseEntity;
    }

    @ExceptionHandler(value = {QueueRequestTimeoutException.class})
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ResponseEntity<VndErrors> queueRequestTimeoutException(QueueRequestTimeoutException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.GATEWAY_TIMEOUT);
        return responseEntity;
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> noHandlerFoundException(NoHandlerFoundException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(value = {InvalidDateFormatException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> invalidDateFormatException(InvalidDateFormatException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {TupleNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> tupleNotFoundException(TupleNotFoundException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(value = {EmptyInventoryException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> emptyInventoryException(EmptyInventoryException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {InvalidCustomerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> invalidCustomerException(InvalidCustomerException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(value = {InvalidItemQuantityException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> invalidItemQuantityException(InvalidItemQuantityException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {ProductUnavailableException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> productUnavailableException(ProductUnavailableException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    @ExceptionHandler(value = {AssertionError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> assertionError(AssertionError e,
                                                                        WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(value = {MicroserviceUnavailableException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> microserviceUnavailableException(MicroserviceUnavailableException e,
                                                    WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(value = {RequestException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> requestException(RequestException e,
                                                    WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(value = {UnsupportedDomainReturnType.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> unsupportedDomainReturnType(UnsupportedDomainReturnType e,
                                                      WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(value = {UnsupportedHttpStatusException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<VndErrors> unsupportedHttpStatusException(UnsupportedHttpStatusException e,
                                                      WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

//    @ExceptionHandler(value = {Throwable.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<VndErrors> throwable(Throwable e,
//                                                    WebRequest request) {
//        VndErrors error = new VndErrors(request.toString(), e.getMessage());
//        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//        return responseEntity;
//    }
}
