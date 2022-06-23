package dev.rmaiun.cpm.dto;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;
import java.util.Map;

public record DomainConfigurationDto(
    String code,
    String parent,
    Map<String, String> implicitConfiguration,
    Map<String, DomainAuthorizationType> directConfiguration) {

  public static final Validator<DomainConfigurationDto> validator =
      ValidatorBuilder.<DomainConfigurationDto>of()
          .constraint(DomainConfigurationDto::code, "code", c -> c.notNull().notBlank())
          .constraint(DomainConfigurationDto::parent, "parent", c -> c.notNull().notBlank())
          .build();
}
