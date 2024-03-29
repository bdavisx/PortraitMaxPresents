package org.loosefx.events;

import com.google.inject.Inject;
import org.loosefx.eventbus.EventService;

public class EventContainer {
    private final EventService eventService;

    @Inject
    public EventContainer( EventService eventBus ) {
        this.eventService = eventBus;
    }

    public void addEvent( Object event ) {
        eventService.publish( event );
    }
}
