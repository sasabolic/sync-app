package com.example.adapter.secondary.salesforce;

import com.example.adapter.secondary.salesforce.client.authentication.AuthenticationClient;
import com.example.domain.model.Authentication;
import com.example.domain.model.Customer;
import com.example.port.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Component
public class AuthenticationAdapter implements AuthenticationService {

  private final AuthenticationClient authenticationClient;

  @Override
  public Mono<Authentication> authenticate(Customer customer) {
    return authenticationClient.login(customer.getClientId().getValue(), customer.getUsername().getValue())
        .map(authenticationResponse -> new Authentication(authenticationResponse.getTokenValue(),
            authenticationResponse.getInstanceUrl()));
  }
}
