package com.portraitmax.presentation;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SalesPresentation extends AbstractAnnotatedAggregateRoot<UUID> {

  @AggregateIdentifier
  private UUID presentationId;
  private List<File> eventImageFiles = new ArrayList<>();

  public SalesPresentation() {}

  @CommandHandler
  public SalesPresentation( CreatePresentationCommand command ) {
    apply( new PresentationCreatedEvent( command.getPresentationId() ) );
  }

  public UUID getIdentifier() { return presentationId; }

  @CommandHandler
  public void handle( AddFilesToPresentationCommand command ) {
    apply( new FilesAddedToPresentationEvent( presentationId, command.getFilesToAdd() ) );
  }

  @EventHandler
  private void applyPresentationCreatedEvent( final PresentationCreatedEvent event ) {
    this.presentationId = event.getPresentationId();
  }

  @EventHandler
  private void applyFilesAddedToPresentationEvent( final FilesAddedToPresentationEvent event ) {
    eventImageFiles.addAll( event.getFilesToAdd() );
  }
}
