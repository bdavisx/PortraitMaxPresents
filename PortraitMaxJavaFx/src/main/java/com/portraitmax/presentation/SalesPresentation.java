package com.portraitmax.presentation;


import org.loosefx.domain.commands.ApplicationCommandHandler;
import org.loosefx.events.ApplicationEventHandler;
import org.loosefx.eventsourcing.DomainEvent;
import org.loosefx.eventsourcing.aggregate.AbstractAggregateRoot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SalesPresentation extends AbstractAggregateRoot {

  private UUID presentationId;
  private List<File> eventImageFiles = new ArrayList<>();

  public SalesPresentation() {
  }

  public UUID getIdentifier() { return presentationId; }
  void setPresentationId( UUID presentationId ) { this.presentationId = presentationId; }

  @ApplicationCommandHandler
  public void handle( CreatePresentationCommand command ) {
    apply( new PresentationCreatedEvent( UUID.randomUUID(), command.getPresentationId() ) );
  }

  @ApplicationCommandHandler
  public void handle( AddFilesToPresentationCommand command ) {
    apply( new FilesAddedToPresentationEvent( UUID.randomUUID(), presentationId, command.getFilesToAdd() ) );
  }

  @ApplicationEventHandler
  private void applyPresentationCreatedEvent( final PresentationCreatedEvent event ) {
    setPresentationId( event.getPresentationId() );
  }

  @ApplicationEventHandler
  private void applyFilesAddedToPresentationEvent( final FilesAddedToPresentationEvent event ) {
    eventImageFiles.addAll( event.getFilesToAdd() );
  }
}
