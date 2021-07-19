package com.example.port;

import com.example.domain.model.SyncDataObjectCommand;
import reactor.core.publisher.Mono;

/**
 * Used to sync data object with Salesforce.
 *
 * @author Saša Bolić
 */
public interface SyncDataObjectProcessor {

  /**
   * Executes command used to initiate sync of data objects.
   *
   * @param syncDataObjectCommand the sync data object command
   * @return the mono
   */
  Mono<Void> execute(SyncDataObjectCommand syncDataObjectCommand);
}
