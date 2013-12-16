package org.loosefx.commands;

import org.loosefx.distributors.ObjectDistributor;

import java.util.function.Consumer;

public class CommandDistributor {
  private final ObjectDistributor objectDistributor;

  public CommandDistributor( ObjectDistributor objectDistributor ) {
    this.objectDistributor = objectDistributor;
  }

  public <T> void register( Class<T> messageClass, Consumer<T> consumer ) {
    objectDistributor.removeAllConsumersForMessageClass( messageClass );
    objectDistributor.registerOnlyForMessageClass( messageClass, consumer );
  }

  public <T> void unregister( Class<T> messageClass, Consumer<T> consumer ) {
    objectDistributor.unregister( messageClass, consumer );
  }

  public void send( Object command ) {
    if( !objectDistributor.doesMessageClassHaveConsumers( command.getClass() ) ) {
      throw new NoHandlerForCommandException( command );
    }
    objectDistributor.send( command );
  }
}
