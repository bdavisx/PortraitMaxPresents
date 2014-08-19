package org.loosefx.eventsourcing;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface EntityEventProvider {
  void clear();
  void loadFromHistory( Iterable<DomainEvent> domainEvents);
  void hookUpVersionProvider( VersionProvider versionProvider);
  Iterable<DomainEvent> getChanges();
  UUID getId();
}
