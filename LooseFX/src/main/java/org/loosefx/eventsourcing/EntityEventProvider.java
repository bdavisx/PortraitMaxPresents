package org.loosefx.eventsourcing;

import java.util.Collection;
import java.util.UUID;

public interface EntityEventProvider {
  void clear();
  void loadFromHistory(Collection<DomainEvent> domainEvents);
  void hookUpVersionProvider( VersionProvider versionProvider);
  Collection<DomainEvent> getChanges();
  UUID getId();
}
