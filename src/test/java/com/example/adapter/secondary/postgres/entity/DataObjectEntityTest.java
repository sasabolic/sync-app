package com.example.adapter.secondary.postgres.entity;

import static com.example.domain.model.DataObjectState.PENDING;
import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.RawData;
import org.junit.jupiter.api.Test;

class DataObjectEntityTest {

  @Test
  void whenCreateDefault_thenNewInstance() {
    DataObjectEntity result = new DataObjectEntity();

    assertThat(result).isNotNull();
  }

  @Test
  void whenCreate_thenNewInstance() {
    DataObject dataObject = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        RawData.of("{mock_data}"),
        1L
    );

    DataObjectEntity result = new DataObjectEntity(dataObject);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", "mock9000007lEieAAE")
        .hasFieldOrPropertyWithValue("customerId", 1L)
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", "{mock_data}")
        .hasFieldOrPropertyWithValue("isNew", true);
  }

  @Test
  void givenNewInstance_whenIsNew_thenTrue() {
    DataObject dataObject = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        RawData.of("{mock_data}"),
        1L
    );

    DataObjectEntity result = new DataObjectEntity(dataObject);

    assertThat(result).isNotNull();
    assertThat(result.isNew()).isTrue();
  }

  @Test
  void whenUpdate_thenNewInstance() {
    DataObject dataObject = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        RawData.of("{mock_data}"),
        1L
    );

    DataObjectEntity entity = new DataObjectEntity();

    DataObjectEntity result = entity.update(dataObject);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", "mock9000007lEieAAE")
        .hasFieldOrPropertyWithValue("customerId", 1L)
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", "{mock_data}")
        .hasFieldOrPropertyWithValue("isNew", false);
  }

  @Test
  void givenRawDataIsNull_whenUpdate_thenNewInstance() {
    DataObject dataObject = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        null,
        1L
    );

    DataObjectEntity entity = new DataObjectEntity();

    DataObjectEntity result = entity.update(dataObject);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", "mock9000007lEieAAE")
        .hasFieldOrPropertyWithValue("customerId", 1L)
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", null)
        .hasFieldOrPropertyWithValue("isNew", false);
  }

  @Test
  void whenToDataObject_thenNewInstance() {
    DataObject dataObject = new DataObject(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        PENDING,
        RawData.of("{mock_data}"),
        1L
    );

    DataObject result = new DataObjectEntity(dataObject).toDataObject();

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", new DataObjectId("mock9000007lEieAAE"))
        .hasFieldOrPropertyWithValue("type", ACCOUNT)
        .hasFieldOrPropertyWithValue("state", PENDING)
        .hasFieldOrPropertyWithValue("rawData", RawData.of("{mock_data}"))
        .hasFieldOrPropertyWithValue("customerId", 1L);
  }
}