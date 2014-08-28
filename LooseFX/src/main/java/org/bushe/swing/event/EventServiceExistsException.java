package org.bushe.swing.event;

/** Exception thrown by the EventServiceLocator when an EventService already is registered for a name. */
public class EventServiceExistsException extends Exception {
    public EventServiceExistsException( final String msg ) {
        super( msg );
    }
}
