package org.javafxmax.commands;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class CommandDistributor {
  private final Map< Class, Consumer > classToConsumerMap = new ConcurrentHashMap<>();

  public <T> void register( Class<T> messageClass, Consumer<T> consumer ) {
    classToConsumerMap.put( messageClass, consumer );
  }

  public void send( Object command ) {
    findConsumer( command ).accept( command );
  }

  private Consumer findConsumer( Object command ) {
    Consumer consumer = classToConsumerMap.get( command.getClass() );
    if( consumer == null ) throw new NoHandlerForCommandException( command );
    return consumer;
  }

}
