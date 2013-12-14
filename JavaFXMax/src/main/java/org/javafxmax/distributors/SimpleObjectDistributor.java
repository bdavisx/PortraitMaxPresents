package org.javafxmax.distributors;

import org.javafxmax.commands.NoHandlerForCommandException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SimpleObjectDistributor implements ObjectDistributor {
  private final Map< Class, Consumer > classToConsumerMap = new ConcurrentHashMap<>();

  @Override public <T> void register( Class<T> messageClass, Consumer<T> consumer ) {
    classToConsumerMap.put( messageClass, consumer );
  }

  @Override public void send( Object command ) {
    findConsumer( command ).accept( command );
  }

  private Consumer findConsumer( Object command ) {
    Consumer consumer = classToConsumerMap.get( command.getClass() );
    if( consumer == null ) throw new NoHandlerForCommandException( command );
    return consumer;
  }
}
