package dev.rmaiun.cpm.utils;

import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.dto.DomainAuthorizationType;
import java.util.Set;

public class RoleTypeMapper {

  private RoleTypeMapper() {
  }

  public static Set<RoleType> authRoleToDomainTypes(DomainAuthorizationType authType) {
    return switch (authType) {
      case R -> Set.of(RoleType.Reader);
      case W -> Set.of(RoleType.Writer);
      case RW -> Set.of(RoleType.Reader, RoleType.Writer);
    };
  }
}
