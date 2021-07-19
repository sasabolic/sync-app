package com.example.domain.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Command used to fetch sync candidates.
 *
 * @author Saša Bolić
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchCandidatesCommand {

  Long customerId;

  Authentication authentication;

  DataObjectType dataObjectType;

  Instant objectLatestSyncDate;

  public FetchCandidatesCommand(SyncDataObjectCommand command) {
    this(
        command.getCustomerId(),
        command.getAuthentication(),
        command.getDataObjectType(),
        command.getObjectLatestSyncDate()
    );
  }
}
