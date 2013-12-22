package org.loosefx.eventsourcing;

import java.util.Collection;
import java.util.UUID;

public interface EventProvider {
  void Clear();
  void loadFromHistory( Collection<DomainEvent> domainEvents );
  void updateVersion( AggregateVersion version );
  UUID getId();
  AggregateVersion getVersion();
  Collection<DomainEvent> getChanges();
}
