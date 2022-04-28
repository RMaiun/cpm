package dev.rmaiun.cpm.exception;

public class UserHasNoRightsException extends BusinessException {

  public static final String CODE = "UserHaveNoRights";

  public UserHasNoRightsException(String message) {
    super(CODE, message);
  }
}
