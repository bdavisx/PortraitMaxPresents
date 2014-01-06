package com.portraitmax.presentation;

import org.loosefx.domain.commands.ApplicationCommand;

import java.util.UUID;

public class CreatePresentationCommand implements ApplicationCommand {
  private final UUID commandId;
  private final UUID presentationId;

  public CreatePresentationCommand(final UUID commandId, final UUID presentationId ) {
    this.commandId = commandId;
    this.presentationId = presentationId;
  }

  @Override
  public UUID getCommandId() { return commandId; }

  public UUID getPresentationId() { return presentationId; }
}
