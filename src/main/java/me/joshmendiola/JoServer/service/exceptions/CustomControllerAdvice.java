package me.joshmendiola.JoServer.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomControllerAdvice
{
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomErrorResponse> handleNullPointerExceptions(Exception e)
    {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new CustomErrorResponse(status, e.getMessage()), status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentExceptions(Exception e)
    {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomErrorResponse(status, e.getMessage()), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleExceptions(Exception e)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        return new ResponseEntity<>(new CustomErrorResponse(status, e.getMessage(), stackTrace), status);
    }
}
