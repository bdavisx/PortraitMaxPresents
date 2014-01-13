package com.portraitmax.presentation;

import java.util.UUID;

public abstract class AbstractPresentationEvent {
  protected UUID presentationId;

  public AbstractPresentationEvent( final UUID presentationId ) {
    this.presentationId = presentationId;
  }

  public UUID getPresentationId() {return presentationId;}
}
