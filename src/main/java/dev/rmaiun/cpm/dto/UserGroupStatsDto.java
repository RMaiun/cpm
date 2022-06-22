package dev.rmaiun.cpm.dto;

import java.util.List;

public record UserGroupStatsDto(String app, String user, List<String> existedAssignments) {

}
