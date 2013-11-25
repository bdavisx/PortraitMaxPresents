package com.portraitmax.presentation;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.javafxmax.domain.commands.ApplicationCommandHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SalesPresentation {
  private final EventBus eventBus;

  private UUID presentationId;
  private List<File> eventImageFiles = new ArrayList<>(  );

  @Inject
  public SalesPresentation( EventBus eventBus ) {this.eventBus = eventBus;}

  public UUID getIdentifier() { return presentationId; }
  void setPresentationId( UUID presentationId ) { this.presentationId = presentationId; }

  @ApplicationCommandHandler
  public void handle( AddFilesToPresentationCommand command ) {
    eventImageFiles.addAll( command.getFilesToAdd() );
    eventBus.post( new FilesAddedToPresentationEvent( getIdentifier(), command.getFilesToAdd() ) );
  }
}
