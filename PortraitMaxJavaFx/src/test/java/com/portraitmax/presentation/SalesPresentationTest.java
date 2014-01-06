package com.portraitmax.presentation;

import org.loosefx.events.EventContainer;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class SalesPresentationTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void itShouldAddFiles() throws Exception {
    File file1 = PowerMockito.mock( File.class );
    File file2 = PowerMockito.mock( File.class );
    final ArrayList<File> filesUserChose = new ArrayList<>( Arrays.asList( new File[]{ file1, file2 } ) );

    UUID presentationId = UUID.randomUUID();
    EventContainer eventContainer = PowerMockito.mock( EventContainer.class );

    SalesPresentation presentation = new SalesPresentation();
    presentation.handle( new CreatePresentationCommand( UUID.randomUUID(), presentationId ) );
    presentation.setPresentationId( presentationId );

    AddFilesToPresentationCommand command = new AddFilesToPresentationCommand( UUID.randomUUID(), presentationId,
      filesUserChose );
    presentation.handle( command );

    verify( eventContainer ).addEvent( new FilesAddedToPresentationEvent( any(), presentationId, filesUserChose ) );
  }

}
