package com.example.adapter.secondary.salesforce.config;

import com.example.adapter.secondary.salesforce.client.authentication.OAuthProperties;
import com.example.adapter.secondary.salesforce.client.authentication.jwt.JwtProperties;
import com.example.adapter.secondary.salesforce.client.object.ClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Salesforce adapter config.
 *
 * @author Saša Bolić
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({JwtProperties.class, OAuthProperties.class, ClientProperties.class})
public class SalesforceConfig {

  @Qualifier("salesforce-client")
  @Bean
  WebClient webClient(WebClient.Builder builder, ClientProperties properties) {
    HttpClient httpClient = HttpClient.create()
        .secure(spec -> spec.sslContext(SslContextBuilder.forClient()))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
            (int) properties.getConnectionTimoutLimit().toMillis())
        .keepAlive(properties.isConnectionKeepAlive())
        .doOnConnected(conn -> conn.addHandlerLast(
            new ReadTimeoutHandler(properties.getReadTimeoutLimit().toMillis(),
                TimeUnit.MILLISECONDS)))
        .headers(entries -> {
          if (properties.isCompressionEnabled()) {
            entries.add(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
          }
        })
        .compress(properties.isCompressionEnabled());

    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
        .codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(properties.getBufferLimit() * 1024 * 1024))
        .build();

    return builder.exchangeStrategies(exchangeStrategies)
        .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}