package dev.rmaiun.cpm.exception;

public class AppNotFoundException extends BusinessException {

  public static final String CODE = "AppNotFound";

  public AppNotFoundException(String app) {
    super(CODE, String.format("Application %s is not found", app));
  }
}
