package dev.rmaiun.cpm.exception;

public class GroupNotFoundException extends BusinessException {

    public static final String CODE = "GroupNotFound";

    public GroupNotFoundException(String app, String group) {
        super(CODE, String.format("Groups %s are not found for application %s", group, app));
    }
}
