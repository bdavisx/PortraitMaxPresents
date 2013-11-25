package com.portraitmax.presentation;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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

    UUID testId = UUID.randomUUID();
    EventBus eventBus = PowerMockito.mock( EventBus.class );

    SalesPresentation presentation = new SalesPresentation( eventBus );
    presentation.setPresentationId( testId );

    AddFilesToPresentationCommand command = new AddFilesToPresentationCommand( testId, filesUserChose );
    presentation.handle( command );

    verify( eventBus ).post( new FilesAddedToPresentationEvent( testId, filesUserChose ) );
  }

}
