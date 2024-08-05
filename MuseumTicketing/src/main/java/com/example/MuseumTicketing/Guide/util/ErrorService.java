package com.example.MuseumTicketing.Guide.util;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    public ResponseEntity<?> handlerException(Exception e) {
        if (e instanceof NullPointerException){
            return new ResponseEntity<>(new ErrorResponse("Null pointer exception occurred",400), HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            return new ResponseEntity<>(new ErrorResponse("Invalid argument exception occurred",400),HttpStatus.BAD_REQUEST);
        } else if (e instanceof AmazonS3Exception) {
            return new ResponseEntity<>(new ErrorResponse("error deleting image from s3 "+e.getMessage(),500),HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred : "+e.getMessage(),500),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
