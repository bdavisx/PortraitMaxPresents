package com.portraitmax.presentation;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class SalesPresentationTest {
  private FixtureConfiguration fixture;

  @Before
  public void setUp() throws Exception {
    fixture = Fixtures.newGivenWhenThenFixture( SalesPresentation.class );
  }
  @Test
  public void itShouldSendPresentationCreatedEvent() throws Exception {
    UUID presentationId = UUID.randomUUID();

    fixture.given()
      .when( new CreatePresentationCommand( presentationId ) )
      .expectEvents( new PresentationCreatedEvent( presentationId ) );
  }

  @Test
  public void itShouldSendFilesAddedEvent() throws Exception {
    File file1 = PowerMockito.mock( File.class );
    File file2 = PowerMockito.mock( File.class );
    final ArrayList<File> filesUserChose = new ArrayList<>( Arrays.asList( new File[]{ file1, file2 } ) );

    UUID presentationId = UUID.randomUUID();

    fixture.given( new PresentationCreatedEvent( presentationId ) )
      .when( new AddFilesToPresentationCommand( presentationId, filesUserChose ) )
      .expectEvents( new FilesAddedToPresentationEvent( presentationId, filesUserChose ) );
  }

}
