package dev.rmaiun.cpm.dto;

import java.util.List;
import java.util.Map;

public record ApplicationConfigurationDto(String appCode,
                                          List<DomainConfigurationDto> domains,
                                          Map<String, List<String>> relations) {

}
