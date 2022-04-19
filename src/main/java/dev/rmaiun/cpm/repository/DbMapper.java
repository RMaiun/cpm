package dev.rmaiun.cpm.repository;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface DbMapper {

  SqlParameterSource asSource();
}
