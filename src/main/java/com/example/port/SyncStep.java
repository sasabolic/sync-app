package com.example.port;

import com.example.domain.model.SyncDataObjectCommand;
import reactor.core.publisher.Mono;

/**
 * Step used during the sync process.
 *
 * @author Saša Bolić
 */
public interface SyncStep {

  /**
   * Executes step.
   *
   * @param command the command
   * @return the mono
   */
  Mono<Void> execute(SyncDataObjectCommand command);
}
