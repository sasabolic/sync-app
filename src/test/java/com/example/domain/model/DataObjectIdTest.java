package com.example.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class DataObjectIdTest {

  @Test
  void verifyEqualsContract() {
    EqualsVerifier.forClass(DataObjectId.class)
        .verify();
  }

  @ParameterizedTest(name = "#{index} - given incorrect value={0} then throw exception={1} with message={2}")
  @CsvSource({
      "'null', 			        'java.lang.IllegalArgumentException', 'Data object id cannot be empty.'",
      "'', 				          'java.lang.IllegalArgumentException', 'Data object id cannot be empty.'",
      "'00109000007lEieAA', 'java.lang.IllegalArgumentException', 'Data object id size is incorrect.'"
  })
  void givenIncorrectValue_whenNewInstance_thenThrowException(
      @ConvertWith(NullableConverter.class) String value, String exceptionClassName,
      String exceptionMessage) throws ClassNotFoundException {
    assertThatThrownBy(() -> new DataObjectId(value))
        .isInstanceOf(Class.forName(exceptionClassName))
        .hasMessage(exceptionMessage);
  }

  @Test
  void whenNewInstance_thenSuccess() {
    DataObjectId result = new DataObjectId("00109000007lEieAAE");

    assertThat(result).isNotNull();
    assertThat(result.getValue()).hasSize(18);
    assertThat(result.getValue()).isEqualTo("00109000007lEieAAE");
  }
}
