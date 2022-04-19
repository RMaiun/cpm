package dev.rmaiun.cpm.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DbConfig {

  // private final DataSource dataSource;
  //
  // public DbConfig(DataSource dataSource) {
  //   this.dataSource = dataSource;
  // }
  //
  // @Bean
  // public NamedParameterJdbcTemplate jdbcTemplate() {
  //   return new NamedParameterJdbcTemplate(dataSource);
  // }
}
