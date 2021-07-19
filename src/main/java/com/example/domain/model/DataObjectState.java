package com.example.domain.model;

/**
 * Stats of the data object.
 *
 * @author Saša Bolić
 */
public enum DataObjectState {

  /**
   * Object is selected for synchronization.
   */
  PENDING,

  /**
   * Synchronization of object completed.
   */
  COMPLETED
}
