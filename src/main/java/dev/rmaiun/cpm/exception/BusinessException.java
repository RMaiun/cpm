package dev.rmaiun.cpm.exception;

public class BusinessException extends RuntimeException {

  private final String code;
  private final String message;

  public BusinessException(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
