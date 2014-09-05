package com.portraitmax.javafx.imagemanagement;

import com.portraitmax.presentation.AddFilesToPresentationCommand;
import com.portraitmax.presentation.RememberedPresentationSettings;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.loosefx.eventbus.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileChooser.class})
public class AddFilesRequestedCorrelatedGUICommandHandlerTest {

  @Before
  public void setup() {

  }

  @Test
  public void extensionFiltersAdded() throws Exception {
    final FileChooserFactory fileChooserFactory = mock( FileChooserFactory.class );
    final FileChooser fileChooser = PowerMockito.mock( FileChooser.class );
    final ObservableList<FileChooser.ExtensionFilter> extensionFiltersListFromChooser = mock( ObservableList.class );

    final FileExtensionFilterProvider extensionFilterProvider = mock( FileExtensionFilterProvider.class );
    final RememberedPresentationSettings remeberedSettings = mock( RememberedPresentationSettings.class );
    final EventService eventBus = mock( EventService.class );

    when( fileChooserFactory.create() ).thenReturn( fileChooser );
    when( fileChooser.getExtensionFilters() ).thenReturn( extensionFiltersListFromChooser );

    final List<FileChooser.ExtensionFilter> providedExtensionFilters = new ArrayList<>();
    when( extensionFilterProvider.getExtensionFilters() ).thenReturn( providedExtensionFilters );

    final File rememberedDirectory = new File( "c:\\" );
    when( remeberedSettings.getCurrentImageDirectory() ).thenReturn( rememberedDirectory );

    final Window window = mock( Window.class );

    final File file1 = PowerMockito.mock( File.class );
    final File directoryForFile1 = PowerMockito.mock( File.class );
    when( file1.getParentFile() ).thenReturn( directoryForFile1 );

    final File file2 = PowerMockito.mock( File.class );

    final ArrayList<File> filesUserChose = new ArrayList<>( Arrays.asList(
      new File[] { file1, file2 } ) );
    when( fileChooser.showOpenMultipleDialog( window  ) ).thenReturn( filesUserChose );

    final AddFilesRequestedCorrelatedGUICommandHandler handler = new AddFilesRequestedCorrelatedGUICommandHandler(
      fileChooserFactory, extensionFilterProvider, remeberedSettings, eventBus );
    final AddFilesRequestedCorrelatedGUICommand commandToHandle =
      new AddFilesRequestedCorrelatedGUICommand( UUID.randomUUID(), window );
    handler.handle( commandToHandle );

    verify( fileChooser ).setTitle( anyString() );
    verify( extensionFiltersListFromChooser ).addAll( providedExtensionFilters );
    verify( fileChooser ).setInitialDirectory( rememberedDirectory );
    verify( remeberedSettings ).setCurrentImageDirectory( directoryForFile1 );
    verify( eventBus ).publish(
      new AddFilesToPresentationCommand( commandToHandle.getCorrelationIdentifier(), filesUserChose ) );
  }
}
