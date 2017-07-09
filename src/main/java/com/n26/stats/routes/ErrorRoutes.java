package com.n26.stats.routes;

import com.n26.stats.models.CustomError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by payal on 7/8/17.
 */
@ControllerAdvice
public class ErrorRoutes {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorRoutes.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleInvalidRequest(MethodArgumentNotValidException e){
        String code = e.getBindingResult().getAllErrors().get(0).getCode();
        CustomError ce = new CustomError("FAILED VALIDATOR - " + code,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        if(code.equals("Recent")) {
            LOG.error(ce.toString());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(ce, HttpStatus.BAD_REQUEST);
        }
    }
}