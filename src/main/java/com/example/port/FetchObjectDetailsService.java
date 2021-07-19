package com.example.port;

import com.example.domain.model.DataObject;
import com.example.domain.model.FetchObjectDetailsCommand;
import reactor.core.publisher.Mono;

/**
 * Used to fetch data object details i.e. raw data.
 *
 * @author Saša Bolić
 */
public interface FetchObjectDetailsService {

  /**
   * Handles {@link FetchObjectDetailsCommand}.
   *
   * @param command the command
   * @return the mono
   */
  Mono<DataObject> handle(FetchObjectDetailsCommand command);
}
