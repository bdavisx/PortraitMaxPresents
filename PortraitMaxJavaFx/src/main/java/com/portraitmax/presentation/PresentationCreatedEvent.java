package com.portraitmax.presentation;

import org.loosefx.eventsourcing.AggregateVersion;
import org.loosefx.eventsourcing.DomainEvent;

import java.util.UUID;

public class PresentationCreatedEvent extends AbstractPresentationEvent implements DomainEvent {
  public PresentationCreatedEvent( final UUID eventId, final UUID presentationId ) {
    super( eventId, presentationId );
  }

}
