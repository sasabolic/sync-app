package com.example.port;

import com.example.domain.model.DataObject;
import com.example.domain.model.FetchCandidatesCommand;
import reactor.core.publisher.Flux;

/**
 * Used to fetch data object eligible for sync.
 *
 * @author Saša Bolić
 */
public interface FetchSyncCandidatesService {

  /**
   * Handles {@link FetchCandidatesCommand}.
   *
   * @param command the command
   * @return the flux
   */
  Flux<DataObject> handle(FetchCandidatesCommand command);
}
