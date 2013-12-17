package org.loosefx.events;

import org.loosefx.distributors.ObjectDistributor;

import java.util.function.Consumer;

public class ClosureEventBus {
  private final ObjectDistributor objectDistributor;

  public ClosureEventBus( ObjectDistributor objectDistributor ) {
    this.objectDistributor = objectDistributor;
  }

  public <T> void unregister( Class<T> messageClass, Consumer<T> consumer ) {
    objectDistributor.unregister( messageClass, consumer );
  }

  public <T> void registerOnlyForMessageClass( final Class<T> messageClass, final Consumer<T> consumer ) {
    objectDistributor.registerOnlyForMessageClass( messageClass, consumer );
  }

  public <T> void registerForMessageClassAndSubclasses( final Class<T> messageClass, final Consumer<T> consumer ) {
    objectDistributor.registerForMessageClassAndSubclasses( messageClass, consumer );
  }

  public void send( Object message ) {objectDistributor.send( message );}


}
