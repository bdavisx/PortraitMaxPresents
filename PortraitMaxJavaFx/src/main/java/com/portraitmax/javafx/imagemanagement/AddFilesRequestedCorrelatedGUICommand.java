package com.portraitmax.javafx.imagemanagement;

import javafx.stage.Window;
import org.loosefx.mvvm.guicommands.AbstractCorrelatedGUICommand;

import java.util.UUID;

public class AddFilesRequestedCorrelatedGUICommand extends AbstractCorrelatedGUICommand {
  private final Window parentWindow;

  public AddFilesRequestedCorrelatedGUICommand( final UUID correlationIdentifier, final Window parentWindow ) {
    super( correlationIdentifier );
    this.parentWindow = parentWindow;
  }

  public Window getParentWindow() {
    return parentWindow;
  }
}
