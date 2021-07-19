package com.example.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public abstract class JsonFixtureTest {

  protected String readJSON(String fixture) {
    try (InputStream inputStream = getClass().getResourceAsStream(fixture)) {
      return new BufferedReader(
          new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
          .collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new IllegalStateException("Could not load JSON fixture", e);
    }
  }
}
