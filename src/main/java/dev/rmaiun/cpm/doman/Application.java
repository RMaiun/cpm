package dev.rmaiun.cpm.doman;

import dev.rmaiun.cpm.repository.DbMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public record Application(Long id, String code) implements DbMapper {

  @Override
  public SqlParameterSource asSource() {
    return new MapSqlParameterSource("id", id)
        .addValue("code", code);
  }
}
