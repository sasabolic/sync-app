package com.example.port;

/**
 * Used to find customers eligible for sync and to trigger syncing for all the candidates.
 *
 * @author Saša Bolić
 */
public interface SyncJob {

  /**
   * Run sync.
   */
  void run();
}
