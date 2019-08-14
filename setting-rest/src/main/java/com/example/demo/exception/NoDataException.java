package com.example.demo.exception;

import java.sql.SQLException;

public class NoDataException extends SQLException {

  private static final long serialVersionUID = 1L;

  public NoDataException(String arg0) {
    super(arg0);
  }
}
