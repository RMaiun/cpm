package dev.rmaiun.cpm.exception;

public class AppAlreadyExistsException extends BusinessException {

  public static final String CODE = "AppAlreadyExists";

  public AppAlreadyExistsException(String app) {
    super(CODE, String.format("App %s already exists", app));
  }
}
