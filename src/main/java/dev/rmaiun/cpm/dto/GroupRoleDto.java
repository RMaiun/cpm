package dev.rmaiun.cpm.dto;

import dev.rmaiun.cpm.doman.RoleType;
import java.util.Set;

public record GroupRoleDto(String group, String domain, Set<RoleType> roleTypes) {}
