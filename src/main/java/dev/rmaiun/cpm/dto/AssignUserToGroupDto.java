package dev.rmaiun.cpm.dto;

import java.util.Set;

public record AssignUserToGroupDto(String app, String user, Set<String> groups) {

}
