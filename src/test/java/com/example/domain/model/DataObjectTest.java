package com.example.domain.model;

import static com.example.domain.model.DataObjectState.COMPLETED;
import static com.example.domain.model.DataObjectState.PENDING;
import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DataObjectTest {

  @Test
  void whenCreate_thenRequiredFieldsSet() {
    DataObject result = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", new DataObjectId("mock9000007lEieAAE"))
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING);
  }

  @Test
  void givenIdEmpty_whenCreate_thenThrowException() {
    assertThatThrownBy(() -> DataObject.of(null, ACCOUNT))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Id cannot be null.");
  }

  @Test
  void givenTypeIsNull_whenCreate_thenThrowException() {
    DataObjectId dataObjectId = new DataObjectId("mock9000007lEieAAE");

    assertThatThrownBy(() -> DataObject.of(dataObjectId, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Type cannot be null.");
  }

  @Test
  void whenCreateFull_thenRequiredFieldsSet() {
    DataObject result = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        RawData.of("{mock_data}"),
        1L
    );

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", new DataObjectId("mock9000007lEieAAE"))
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", RawData.of("{mock_data}"))
        .hasFieldOrPropertyWithValue("customerId", 1L);
  }

  @Test
  void whenCreateWithRawData_thenRequiredFieldsSet() {
    DataObject result = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT, RawData.of("{mock_data}"));

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", new DataObjectId("mock9000007lEieAAE"))
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", RawData.of("{mock_data}"));
  }

  @Test
  void givenRawDataNull_whenCreateWithRawData_thenThrowException() {
    DataObjectId dataObjectId = new DataObjectId("mock9000007lEieAAE");

    assertThatThrownBy(() -> DataObject.of(dataObjectId, ACCOUNT, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("RawData cannot be null.");
  }

  @Test
  void whenAssignCustomer_thenSuccess() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);

    DataObject result = dataObject.assignCustomer(1L);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("customerId", 1L);
  }

  @Test
  void givenCustomerIdIsNull_whenAssignCustomer_thenThrowException() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);

    assertThatThrownBy(() -> dataObject.assignCustomer(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("CustomerId cannot be null.");
  }

  @Test
  void whenComplete_thenStateChangedToCompleted() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);

    DataObject result = dataObject.complete();

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("state", COMPLETED);
  }
}