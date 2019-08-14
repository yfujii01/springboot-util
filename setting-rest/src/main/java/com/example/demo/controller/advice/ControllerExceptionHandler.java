package com.example.demo.controller.advice;

import com.example.demo.exception.NoDataException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {
  /**
   * 404
   *
   * @param ex throwされたException
   * @param request the current request
   * @return エラーレスポンス
   */
  @ExceptionHandler({NoDataException.class})
  public ResponseEntity<Object> handle404(NoDataException ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }
}
