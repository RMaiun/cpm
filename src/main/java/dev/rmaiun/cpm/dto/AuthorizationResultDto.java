package dev.rmaiun.cpm.dto;

import java.util.Collections;
import java.util.List;

public record AuthorizationResultDto(boolean success, List<String> userAssignedGroups) {

  public static AuthorizationResultDto success(List<String> userAssignedGroups) {
    return new AuthorizationResultDto(true, userAssignedGroups);
  }

  public static AuthorizationResultDto failure() {
    return new AuthorizationResultDto(false, Collections.emptyList());
  }
}
