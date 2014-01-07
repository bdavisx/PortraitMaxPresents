package com.portraitmax.presentation;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.ThreadSafeEventService;
import org.loosefx.events.ApplicationEventHandler;
import org.loosefx.events.EventContainer;
import org.junit.Before;
import org.junit.Test;
import org.loosefx.eventsourcing.DomainEvent;
import org.loosefx.registrars.InstantiatedObjectAnnotatedStrategyBasedRegistrar;
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
  private List<DomainEvent> receivedEvents = new ArrayList<>();

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void itShouldSendPresentationCreatedEvent() throws Exception {
    File file1 = PowerMockito.mock( File.class );
    File file2 = PowerMockito.mock( File.class );
    final ArrayList<File> filesUserChose = new ArrayList<>( Arrays.asList( new File[]{ file1, file2 } ) );

    UUID presentationId = UUID.randomUUID();

    SalesPresentation presentation = createBasicPresentation( presentationId );

    assertThat( receivedEvents.stream().anyMatch( e -> e.getClass().equals( PresentationCreatedEvent.class ) ) )
      .isTrue();
  }

  @Test
  public void itShouldSendFilesAddedEvent() throws Exception {
    File file1 = PowerMockito.mock( File.class );
    File file2 = PowerMockito.mock( File.class );
    final ArrayList<File> filesUserChose = new ArrayList<>( Arrays.asList( new File[]{ file1, file2 } ) );

    UUID presentationId = UUID.randomUUID();

    SalesPresentation presentation = createBasicPresentation( presentationId );

    AddFilesToPresentationCommand command = new AddFilesToPresentationCommand( UUID.randomUUID(), presentationId,
      filesUserChose );
    presentation.handle( command );

    assertThat( receivedEvents.stream().anyMatch( e -> e.getClass().equals( FilesAddedToPresentationEvent.class ) ) )
      .isTrue();
  }

  private SalesPresentation createBasicPresentation( final UUID presentationId ) {
    SalesPresentation presentation = new SalesPresentation();
    registerEventMethods( presentation );
    EventService eventBus = createEventBus();
    presentation.setEventBus( eventBus );
    presentation.handle( new CreatePresentationCommand( UUID.randomUUID(), presentationId ) );
    return presentation;
  }

  private EventService createEventBus() {
    EventService eventBus = new ThreadSafeEventService();
    EventSubscriber<PresentationCreatedEvent> createdSubscriber = new EventSubscriber<PresentationCreatedEvent>() {
      @Override
      public void onEvent( final PresentationCreatedEvent event ) {
        receivedEvents.add( event );
      }
    };
    eventBus.subscribe( PresentationCreatedEvent.class, createdSubscriber );
    EventSubscriber<FilesAddedToPresentationEvent> filesAddedSubscriber =
      new EventSubscriber<FilesAddedToPresentationEvent>() {
        @Override
        public void onEvent( final FilesAddedToPresentationEvent event ) {
          receivedEvents.add( event );
        }
      };
    eventBus.subscribe( FilesAddedToPresentationEvent.class, filesAddedSubscriber );
    return eventBus;
  }

  private void registerEventMethods( final SalesPresentation presentation ) {
    InstantiatedObjectAnnotatedStrategyBasedRegistrar registrar =
      new InstantiatedObjectAnnotatedStrategyBasedRegistrar( ApplicationEventHandler.class );
    registrar.registerAnnotatedHandlers( presentation, presentation::registerEvent );
  }

}
