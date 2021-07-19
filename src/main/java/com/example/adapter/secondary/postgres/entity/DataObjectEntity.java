package com.example.adapter.secondary.postgres.entity;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectState;
import com.example.domain.model.DataObjectType;
import com.example.domain.model.RawData;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Customer entity mapping for Postgres DB.
 *
 * @author Saša Bolić
 */
@NoArgsConstructor
@Getter
@ToString
@Table("data_object")
public class DataObjectEntity implements Persistable<String> {

  @Id
  @Column("id")
  private String id;

  @Column("customer_id")
  private Long customerId;

  @CreatedDate
  @Column("created_on")
  @SuppressWarnings("unused")
  private Instant createdOn;

  @LastModifiedDate
  @Column("modified_on")
  @SuppressWarnings("unused")
  private Instant modifiedOn;

  @Column("state")
  private DataObjectState state;

  @Column("type")
  private DataObjectType type;

  @Column("raw_data")
  private String rawData;

  @Transient
  private boolean isNew;

  public DataObjectEntity(DataObject dataObject) {
    this.id = dataObject.getId().getValue();
    this.customerId = dataObject.getCustomerId();
    this.state = dataObject.getState();
    this.type = dataObject.getType();
    this.rawData = dataObject.getRawData() != null ? dataObject.getRawData().getValue() : null;
    this.isNew = true;
  }

  public DataObjectEntity update(DataObject dataObject) {
    this.id = dataObject.getId().getValue();
    this.customerId = dataObject.getCustomerId();
    this.state = dataObject.getState();
    this.type = dataObject.getType();
    this.rawData = dataObject.getRawData() != null ? dataObject.getRawData().getValue() : null;
    this.isNew = false;

    return this;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  public DataObject toDataObject() {
    return new DataObject(
        new DataObjectId(this.id),
        this.type,
        this.state,
        this.rawData != null ? RawData.of(this.rawData) : null,
        this.customerId
    );
  }
}
