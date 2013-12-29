package org.loosefx.eventsourcing;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public interface EntityEventProvider {
  void clear();
  void loadFromHistory( Stream<DomainEvent> domainEvents);
  void hookUpVersionProvider( VersionProvider versionProvider);
  Stream<DomainEvent> getChanges();
  UUID getId();
}
