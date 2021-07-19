package com.example.adapter.primary.rest;

import com.example.adapter.primary.rest.dto.CreateCustomerRequest;
import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import com.example.port.CustomerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Customer resource.
 *
 * @author Saša Bolić
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public Mono<ResponseEntity<Void>> create(
      @RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
      return customerService.create(
        Customer.of(Username.of(createCustomerRequest.getUsername()),
            ClientId.of(createCustomerRequest.getClientId())))
        .map(customer -> UriComponentsBuilder
            .fromPath("/customers/{id}")
            .buildAndExpand(customer.getId()).toUri())
        .map(location -> ResponseEntity.created(location).build());
  }
}
