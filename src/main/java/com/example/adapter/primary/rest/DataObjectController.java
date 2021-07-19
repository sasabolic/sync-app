package com.example.adapter.primary.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.RawData;
import com.example.port.DataObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Data object resource.
 *
 * @author Saša Bolić
 */
@RestController
@RequestMapping("/salesforce")
@RequiredArgsConstructor
public class DataObjectController {

  private final DataObjectService dataObjectService;

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public Mono<String> getRawData(@PathVariable String id) {
    return dataObjectService.findCompletedById(new DataObjectId(id))
        .switchIfEmpty(Mono.empty())
        .map(DataObject::getRawData)
        .map(RawData::getValue);
  }
}
