package com.example.adapter.secondary.postgres.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

/**
 * Database config.
 *
 * @author Saša Bolić
 */
@Configuration(proxyBeanMethods = false)
@EnableR2dbcAuditing
public class DatabaseConfig {

  private final Environment env;

  public DatabaseConfig(final Environment env) {
    this.env = env;
  }

  @Bean(initMethod = "migrate")
  public Flyway flyway() {
    return new Flyway(Flyway.configure()
        .baselineOnMigrate(true)
        .dataSource(
            env.getRequiredProperty("spring.flyway.url"),
            env.getRequiredProperty("spring.flyway.user"),
            env.getRequiredProperty("spring.flyway.password"))
    );
  }
}
  