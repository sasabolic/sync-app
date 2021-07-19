package com.example.domain.model;

import static org.springframework.util.Assert.notNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data object being synced.
 *
 * @author Saša Bolić
 */
@AllArgsConstructor
@Getter
public class DataObject {

  private final DataObjectId id;

  private final DataObjectType type;

  private DataObjectState state;

  private RawData rawData;

  private Long customerId;

  public static DataObject of(DataObjectId id, DataObjectType type, RawData rawData) {
    return new DataObject(id, type, rawData);
  }

  public static DataObject of(DataObjectId id, DataObjectType type) {
    return new DataObject(id, type);
  }

  private DataObject(DataObjectId id, DataObjectType type) {
    notNull(id, "Id cannot be null.");
    notNull(type, "Type cannot be null.");

    this.id = id;
    this.type = type;
    this.state = DataObjectState.PENDING;
  }

  private DataObject(DataObjectId id, DataObjectType type, RawData rawData) {
    this(id, type);

    notNull(rawData, "RawData cannot be null.");

    this.rawData = rawData;
  }

  public DataObject assignCustomer(Long customerId)  {
    notNull(customerId, "CustomerId cannot be null.");

    this.customerId = customerId;

    return this;
  }

  public DataObject complete() {
    this.state = DataObjectState.COMPLETED;

    return this;
  }

}
