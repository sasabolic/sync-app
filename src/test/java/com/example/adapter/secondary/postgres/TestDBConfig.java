package com.example.adapter.secondary.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;

@DataR2dbcTest
class TestDBConfig {

  @Autowired
  protected DatabaseClient databaseClient;

  @BeforeEach
  void setUp() {
    cleanupDB();
  }

  protected void executeSQL(String sql) {
    databaseClient
        .sql(sql)
        .then().block();
  }

  protected void cleanupDB() {
    executeSQL("DELETE FROM data_object");
    executeSQL("DELETE FROM customer");
  }
}
