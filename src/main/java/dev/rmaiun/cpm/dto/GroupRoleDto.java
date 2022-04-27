package dev.rmaiun.cpm.dto;

import dev.rmaiun.cpm.doman.RoleType;
import java.util.List;

public record GroupRoleDto(String group, String domain, List<RoleType> roleTypes) {

}
