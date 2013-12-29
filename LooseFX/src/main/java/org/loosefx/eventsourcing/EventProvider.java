package org.loosefx.eventsourcing;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public interface EventProvider {
  void Clear();
  void loadFromHistory( Stream<DomainEvent> domainEvents );
  void updateVersion( AggregateVersion version );
  UUID getId();
  AggregateVersion getVersion();
  Stream<DomainEvent> getChanges();
}
