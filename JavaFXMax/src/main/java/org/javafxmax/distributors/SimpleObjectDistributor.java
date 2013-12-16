package org.javafxmax.distributors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleObjectDistributor implements ObjectDistributor {
  private final ConsumerSelectorFactory consumerSelectorFactory;
  private final List<SendMessageIfMatches> messageSenders = new ArrayList<>();

  public SimpleObjectDistributor( ConsumerSelectorFactory consumerSelectorFactory ) {
    this.consumerSelectorFactory = consumerSelectorFactory;
  }

  @Override public <T> void registerOnlyForMessageClass( Class<T> messageClass, Consumer<T> consumer ) {
    MessageConsumerSelector selector = consumerSelectorFactory.createMessageClassOnlyConsumerSelector( messageClass );
    SendMessageIfMatches sender = new SendMessageIfMatches( selector, consumer );
    messageSenders.add( sender );
  }

  @Override public <T> void registerForMessageClassAndSubclasses( Class<T> messageClass, Consumer<T> consumer ) {
    MessageConsumerSelector selector = consumerSelectorFactory.createMessageClassAndSubclassesConsumerSelector(
      messageClass );
    SendMessageIfMatches sender = new SendMessageIfMatches( selector, consumer );
    messageSenders.add( sender );
  }

  @Override public <T> void unregister( Class<T> messageClass, Consumer<T> consumer ) {
    for( SendMessageIfMatches sender : new ArrayList<SendMessageIfMatches>( messageSenders ) ) {
      if( sender.matchesExactly( messageClass, consumer ) ) {
        messageSenders.remove( sender );
      }
    }
  }

  @Override public void send( Object command ) {
    for( SendMessageIfMatches sender : new ArrayList<SendMessageIfMatches>( messageSenders ) ) {
      sender.sendMessage( command );
    }
  }

  @Override public <T> boolean doesMessageClassHaveConsumers( Class<T> messageClass ) {
    return messageSenders.stream().anyMatch( (sender) -> sender.matchesForSending( messageClass ) );
  }
}

