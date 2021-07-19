package com.example.domain.model;

import java.time.Instant;
import lombok.Value;

/**
 * Command used to trigger sync of data objects of given type.
 *
 * @author Saša Bolić
 */
@Value
public class  SyncDataObjectCommand {

  Long customerId;

  Authentication authentication;

  DataObjectType dataObjectType;

  Instant objectLatestSyncDate;
}
