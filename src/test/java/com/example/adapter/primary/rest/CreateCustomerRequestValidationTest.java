package com.example.adapter.primary.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.adapter.primary.rest.dto.CreateCustomerRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateCustomerRequestValidationTest {

  private static Validator validator;

  @BeforeAll
  static void beforeAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenValid_thenNoConstraintViolations() {
    CreateCustomerRequest request = new CreateCustomerRequest("mock_username",
        "mock_client_d");

    Set<ConstraintViolation<CreateCustomerRequest>> result = validator.validate(request);

    assertThat(result).isEmpty();
  }

  @Test
  void whenInvalid_thenConstraintViolationsExist() {
    CreateCustomerRequest request = new CreateCustomerRequest("",
        "");

    Set<ConstraintViolation<CreateCustomerRequest>> result = validator.validate(request);

    assertThat(result)
        .isNotEmpty()
        .hasSize(2);
  }
}