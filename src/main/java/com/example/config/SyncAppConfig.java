package com.example.config;

import com.example.domain.service.sync.SyncProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Sync app config.
 *
 * @author Saša Bolić
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SyncProperties.class})
public class SyncAppConfig {

}
