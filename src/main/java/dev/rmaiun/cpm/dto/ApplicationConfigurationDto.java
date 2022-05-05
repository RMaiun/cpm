package dev.rmaiun.cpm.dto;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;
import java.util.List;
import java.util.Map;

public record ApplicationConfigurationDto(String appCode,
                                          boolean fullReplace,
                                          List<DomainConfigurationDto> domains,
                                          Map<String, List<String>> relations) {

  public static final Validator<ApplicationConfigurationDto> validator = ValidatorBuilder.<ApplicationConfigurationDto>of()
      .constraint(ApplicationConfigurationDto::appCode, "appCode", c -> c.notNull().notBlank())
      .forEach(ApplicationConfigurationDto::domains, "domains", DomainConfigurationDto.validator)
      .build();
}
