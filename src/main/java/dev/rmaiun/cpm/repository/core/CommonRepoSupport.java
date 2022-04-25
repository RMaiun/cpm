package dev.rmaiun.cpm.repository.core;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface CommonRepoSupport<T> {

  SqlParameterSource parameterSource(T object);

  RowMapper<T> rowMapper();
}
