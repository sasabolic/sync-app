package com.example.domain.service.sync;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties used to configure sync process.
 *
 * @author Saša Bolić
 */
@ConstructorBinding
@Getter
@RequiredArgsConstructor
@ConfigurationProperties("sync.app")
public class SyncProperties {

  private final Duration postponeOffset;
  private final Duration syncOnOffset;
  private final Integer customerSyncLimit;
}
