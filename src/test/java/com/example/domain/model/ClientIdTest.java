package com.example.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class ClientIdTest {

  @Test
  void verifyEqualsContract() {
    EqualsVerifier.forClass(ClientId.class)
        .verify();
  }

  @ParameterizedTest(name = "#{index} - given incorrect value={0} then throw exception={1} with message={2}")
  @CsvSource({
      "'null',  'java.lang.IllegalArgumentException', 'Client ID cannot be empty.'",
      "'',      'java.lang.IllegalArgumentException', 'Client ID cannot be empty.'",
  })
  void givenIncorrectValue_whenNewInstance_thenThrowException(
      @ConvertWith(NullableConverter.class) String value, String exceptionClassName,
      String exceptionMessage) throws ClassNotFoundException {
    assertThatThrownBy(() -> ClientId.of(value))
        .isInstanceOf(Class.forName(exceptionClassName))
        .hasMessage(exceptionMessage);
  }

  @Test
  void whenNewInstance_thenSuccess() {
    ClientId result = ClientId.of("client_id");

    assertThat(result).isNotNull();
    assertThat(result.getValue()).isNotEmpty();
  }
}