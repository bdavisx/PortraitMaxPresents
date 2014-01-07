package org.loosefx.eventsourcing.aggregate;

import org.loosefx.eventsourcing.AggregateVersion;
import org.loosefx.eventsourcing.DomainEvent;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface EventProvider {
  void clearEvents();
  void loadFromHistory( Stream<DomainEvent> domainEvents );
  void updateVersion( AggregateVersion version );
  UUID getId();
  AggregateVersion getVersion();
  Stream<DomainEvent> getChanges();

  void registerEvent( Class eventType, Consumer<DomainEvent> eventHandler );
}