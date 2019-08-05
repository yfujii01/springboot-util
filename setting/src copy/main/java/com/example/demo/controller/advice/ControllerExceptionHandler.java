package com.example.demo.controller.advice;

import java.sql.SQLException;

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
   * @param ex      throwされたException
   * @param request the current request
   * @return エラーレスポンス
   */
  @ExceptionHandler({ NoDataException.class })
  public ResponseEntity<Object> handle404(NoDataException ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    // ErrorResponse body = new ErrorResponse("Not Found", "", "");
    HttpStatus status = HttpStatus.NOT_FOUND;

    return this.handleExceptionInternal(ex, "Not Found", headers, status, request);
  }

  // @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, String body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    // if (!(body instanceof ErrorResponse)) {
    //   body = new ErrorResponse(status.getReasonPhrase(), "", "");
    // }
    return new ResponseEntity<>(body, headers, status);
  }
  // /**
  // * 500
  // *
  // * @param ex throwされたException
  // * @param request the current request
  // * @return エラーレスポンス
  // */
  // @ExceptionHandler({ Exception.class })
  // public ResponseEntity<Object> handle500(Exception ex, WebRequest request) {
  // HttpHeaders headers = new HttpHeaders();
  // ErrorResponse body = new ErrorResponse("Internal Server Error", "", "");
  // HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  // return this.handleExceptionInternal(ex, body, headers, status, request);
  // }

  
}