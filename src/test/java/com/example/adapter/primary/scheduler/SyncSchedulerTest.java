package com.example.adapter.primary.scheduler;

import static org.mockito.BDDMockito.then;

import com.example.port.SyncJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncSchedulerTest {

  @Mock
  SyncJob syncJob;

  SyncScheduler syncScheduler;

  @BeforeEach
  void setUp() {
    syncScheduler = new SyncScheduler(
        syncJob
    );
  }

  @Test
  void whenTriggerSync_thenCallSyncJob() {

    syncScheduler.triggerSync();

    then(syncJob).should().run();
  }
}