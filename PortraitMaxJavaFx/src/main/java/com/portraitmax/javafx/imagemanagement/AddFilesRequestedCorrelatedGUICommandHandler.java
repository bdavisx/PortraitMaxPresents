package com.portraitmax.javafx.imagemanagement;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.portraitmax.presentation.AddFilesToPresentationCommand;
import com.portraitmax.presentation.RememberedPresentationSettings;
import javafx.stage.FileChooser;
import org.loosefx.eventbus.EventService;
import org.loosefx.mvvm.guicommands.GUICommandHandler;

import java.io.File;
import java.util.List;

/**
 * Scenario: Fire off correlated message with AddFilesRequestedCorrelatedGUICommand,
 * a "central" coordinator will watch for that message and create the "service". The service
 * will then fire off a correlated message AddFilesSelectedCoordinatedGUICommand. The
 * calling object (or one of its "designees") should watch for and handle the command.
 */
public class AddFilesRequestedCorrelatedGUICommandHandler {
  private final FileChooserFactory fileChooserFactory;
  private final FileExtensionFilterProvider extensionFilterProvider;
  private final RememberedPresentationSettings remeberedSettings;
  private final EventService eventBus;

  @Inject
  public AddFilesRequestedCorrelatedGUICommandHandler( final FileChooserFactory fileChooserFactory,
    final FileExtensionFilterProvider extensionFilterProvider,
    final RememberedPresentationSettings remeberedSettings, final EventService eventBus ) {
    this.fileChooserFactory = fileChooserFactory;
    this.extensionFilterProvider = extensionFilterProvider;
    this.remeberedSettings = remeberedSettings;
    this.eventBus = eventBus;
  }

  @GUICommandHandler
  public void handle( final AddFilesRequestedCorrelatedGUICommand command ) {
    final FileChooser chooser = fileChooserFactory.create();
    configureChooser( chooser );

    final List<File> files = chooser.showOpenMultipleDialog( command.getParentWindow() );

    saveSettings( files );

    eventBus.publish( new AddFilesToPresentationCommand( command.getCorrelationIdentifier(), files ) );
  }

  private void configureChooser( final FileChooser chooser ) {
    chooser.setTitle( "Select Files to Add to the Presentation" );
    chooser.getExtensionFilters().addAll( extensionFilterProvider.getExtensionFilters() );
    chooser.setInitialDirectory( remeberedSettings.getCurrentImageDirectory() );
  }

  private void saveSettings( final List<File> files ) {
    final Optional<File> firstFileOptional = FluentIterable.from( files ).first();
    if( firstFileOptional.isPresent() ) {
      remeberedSettings.setCurrentImageDirectory( firstFileOptional.get().getParentFile() );
    }
  }
}
