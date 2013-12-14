package com.portraitmax.presentation;


import com.google.inject.Inject;
import org.javafxmax.domain.commands.ApplicationCommandHandler;
import org.javafxmax.events.EventContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SalesPresentation {

  private final EventContainer eventContainer;
  private UUID presentationId;
  private List<File> eventImageFiles = new ArrayList<>();

  @Inject
  public SalesPresentation( EventContainer eventContainer ) {
    this.eventContainer = eventContainer;
  }

  public UUID getIdentifier() { return presentationId; }
  void setPresentationId( UUID presentationId ) { this.presentationId = presentationId; }

  @ApplicationCommandHandler
  public void handle( AddFilesToPresentationCommand command ) {
    apply( new FilesAddedToPresentationEvent( presentationId, command.getFilesToAdd() ) );
    eventContainer.addEvent( new FilesAddedToPresentationEvent( getIdentifier(), command.getFilesToAdd() ) );
  }

  public void apply( FilesAddedToPresentationEvent event ) {
    eventImageFiles.addAll( event.getFilesToAdd() );
  }
}
