package dev.rmaiun.cpm.exception;

public class DatabaseException extends BusinessException {

  public static final String CODE = "DatabaseException";

  public DatabaseException(String message) {
    super(CODE, message);
  }
}
