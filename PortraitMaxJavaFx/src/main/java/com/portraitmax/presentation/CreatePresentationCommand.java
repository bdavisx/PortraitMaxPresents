package com.portraitmax.presentation;


import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.UUID;

public class CreatePresentationCommand  {
  @TargetAggregateIdentifier
  private final UUID presentationId;

  public CreatePresentationCommand(final UUID presentationId ) {
    this.presentationId = presentationId;
  }

  public UUID getPresentationId() { return presentationId; }
}
