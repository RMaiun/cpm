package dev.rmaiun.cpm.doman.view;

import dev.rmaiun.cpm.doman.BusinessRole;
import java.util.Set;

public interface GroupView {

  Long getId();

  String getCode();

  Set<BusinessRole> getCastedRoles();
}
