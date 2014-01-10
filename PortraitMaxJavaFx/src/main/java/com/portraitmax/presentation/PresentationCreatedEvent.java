package com.portraitmax.presentation;

import java.util.UUID;

public class PresentationCreatedEvent extends AbstractPresentationEvent {
  public PresentationCreatedEvent( final UUID presentationId ) {
    super( presentationId );
  }

}
