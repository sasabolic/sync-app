package com.example.domain.model;

import static org.springframework.util.Assert.notNull;

import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * Customer who's data objects should be synced.
 *
 * @author Saša Bolić
 */
@Log4j2
@AllArgsConstructor
@Getter
@ToString
public class Customer {

  private Long id;

  private final Username username;

  private final ClientId clientId;

  private Instant syncedOn;

  private Instant postponedUntil;

  public static Customer of(Username username, ClientId clientId) {
    return new Customer(username, clientId);
  }

  private Customer(Username username, ClientId clientId) {
    notNull(username, "Username cannot be null.");
    notNull(clientId, "ClientId cannot be null.");

    this.username = username;
    this.clientId = clientId;
  }

  /**
   * Complete customer i.e. mark him as synced.
   *
   * @param lastSyncDate the last sync date
   * @return the customer
   */
  public Customer complete(Instant lastSyncDate) {
    notNull(lastSyncDate, "LastSyncDate cannot be null.");

    this.syncedOn = lastSyncDate;
    log.debug("Completing sync for customer={}", getId());

    return this;
  }

  /**
   * Postpone customer synchronization.
   *
   * @param offset the offset
   * @return the customer
   */
  public Customer postpone(Duration offset) {
    notNull(offset, "Offset duration cannot be null.");

    this.postponedUntil = Instant.now().plusMillis(offset.toMillis());
    log.debug("Postponing sync for customer={} until {}", getId(), getPostponedUntil());

    return this;
  }
}
