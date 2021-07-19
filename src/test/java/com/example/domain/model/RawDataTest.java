package com.example.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.common.JsonFixtureTest;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class RawDataTest extends JsonFixtureTest {

  @Test
  void verifyEqualsContract() {
    EqualsVerifier.forClass(RawData.class)
        .verify();
  }

  @ParameterizedTest(name = "#{index} - given incorrect value={0} then throw exception={1} with message={2}")
  @CsvSource({
      "'null', 			     'java.lang.IllegalArgumentException', 'Raw data cannot be empty.'",
      "'', 				       'java.lang.IllegalArgumentException', 'Raw data cannot be empty.'",
      "'mock_raw_data',  'java.lang.IllegalArgumentException',  'Raw data format is incorrect.'",
      "'{mock_raw_data',  'java.lang.IllegalArgumentException', 'Raw data format is incorrect.'"
  })
  void givenIncorrectValue_whenNewInstance_thenThrowException(
      @ConvertWith(NullableConverter.class) String value, String exceptionClassName,
      String exceptionMessage) throws ClassNotFoundException {
    assertThatThrownBy(() -> RawData.of(value))
        .isInstanceOf(Class.forName(exceptionClassName))
        .hasMessage(exceptionMessage);
  }

  @Test
  void whenNewInstance_thenSuccess() {
    RawData result = RawData.of(readJSON("/fixtures/contact_details.json"));

    assertThat(result).isNotNull();
    assertThat(result.getValue()).isNotEmpty();
  }
}