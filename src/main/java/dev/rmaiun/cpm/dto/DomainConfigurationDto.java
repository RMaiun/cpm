package dev.rmaiun.cpm.dto;

import java.util.Map;

public record DomainConfigurationDto(String code,
                                     String parent,
                                     Map<String, String> implicitConfiguration,
                                     Map<String, String> directConfiguration) {

}
