package com.appsdeveloperblog.app.ws.mobileappws.exceptions;

import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        String localizedErrorMessage = ex.getLocalizedMessage();

        if (localizedErrorMessage == null) {
            localizedErrorMessage = ex.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                localizedErrorMessage);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({NullPointerException.class, UserServiceException.class})
    public ResponseEntity<Object> handleSpecificExceptions(Exception ex, WebRequest request) {

        String localizedErrorMessage = ex.getLocalizedMessage();

        if (localizedErrorMessage == null) {
            localizedErrorMessage = ex.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                localizedErrorMessage);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

/*    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<Object> handleUserServiceExceptionException(UserServiceException ex, WebRequest request) {

        String localizedErrorMessage = ex.getLocalizedMessage();

        if (localizedErrorMessage == null) {
            localizedErrorMessage = ex.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                localizedErrorMessage);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }*/
}
