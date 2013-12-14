package org.javafxmax.commands;

import org.javafxmax.distributors.ObjectDistributor;
import org.javafxmax.distributors.SimpleObjectDistributor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class CommandDistributor implements ObjectDistributor {
  private final ObjectDistributor objectDistributor;

  public CommandDistributor( ObjectDistributor objectDistributor ) {
    this.objectDistributor = objectDistributor;
  }

  @Override public <T> void register( Class<T> commandClass, Consumer<T> consumer ) {
    objectDistributor.register( commandClass, consumer );
  }

  @Override public void send( Object command ) {objectDistributor.send( command );}
}
