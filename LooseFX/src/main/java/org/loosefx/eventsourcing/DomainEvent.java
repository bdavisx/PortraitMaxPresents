package org.loosefx.eventsourcing;

import java.util.UUID;

public interface DomainEvent {
  UUID getId();
  UUID getAggregateId();
  AggregateVersion getVersion();
}
