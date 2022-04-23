package dev.rmaiun.cpm.doman;

import dev.rmaiun.cpm.repository.CommonRepoSupport;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public record Application(Long id, String code) { }
