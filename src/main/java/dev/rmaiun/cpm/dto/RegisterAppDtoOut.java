package dev.rmaiun.cpm.dto;

import java.util.List;

public record RegisterAppDtoOut(Long id, String code, List<String> owners) {}
