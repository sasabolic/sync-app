package com.example.adapter.secondary.postgres.entity;

import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Customer entity mapping for Postgres DB.
 *
 * @author Saša Bolić
 */
@NoArgsConstructor
@Getter
@Table("customer")
public class CustomerEntity {

  @Id
  @SuppressWarnings("unused")
  private Long id;

  @Column("username")
  private String username;

  @Column("client_id")
  private String clientId;

  @Column("synced_on")
  private Instant syncedOn;

  @Column("sync_postponed_until")
  private Instant postponedUntil;

  public CustomerEntity(Customer customer) {
    this.id = customer.getId();
    this.username = customer.getUsername() != null ? customer.getUsername().getValue() : null;
    this.clientId = customer.getClientId() != null ? customer.getClientId().getValue() : null;
    this.syncedOn = customer.getSyncedOn();
    this.postponedUntil = customer.getPostponedUntil();
  }

  public Customer toCustomer() {
    return new Customer(
        this.id,
        this.username != null ? Username.of(this.username) : null,
        this.clientId != null ? ClientId.of(this.clientId) : null,
        this.syncedOn,
        this.postponedUntil
    );
  }
}
