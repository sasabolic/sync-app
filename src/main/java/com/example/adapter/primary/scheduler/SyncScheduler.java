package com.example.adapter.primary.scheduler;

import com.example.port.SyncJob;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SyncScheduler {

  private final SyncJob syncJob;

  @Scheduled(cron = "${scheduler.cron}")
  public void triggerSync() {
    syncJob.run();
  }
}
