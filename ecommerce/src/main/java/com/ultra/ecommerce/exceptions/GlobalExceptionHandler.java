package com.ultra.ecommerce.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(Exception exception) {
        // TODO: Send this stack trace to an observability tool
        exception.printStackTrace();

        ErrorResponse errorResponse;
        HttpStatus status;

        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "The username or password is incorrect");
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "The account is locked");
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "You are not authorized to access this resource");
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "The JWT signature is invalid");
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "The JWT token has expired");
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = new ErrorResponse(status.value(), exception.getMessage(), "Unknown internal server error.");
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    // Inner class for error response
    public static class ErrorResponse {
        private int statusCode;
        private String message;
        private String description;

        public ErrorResponse(int statusCode, String message, String description) {
            this.statusCode = statusCode;
            this.message = message;
            this.description = description;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
