package org.loosefx.distributors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleObjectDistributor implements ObjectDistributor {
  private final ConsumerSelectorFactory consumerSelectorFactory;
  private final List<SendMessageIfMatches> messageSenders = new ArrayList<>();

  // TODO: make multi-threaded?

  public SimpleObjectDistributor( ConsumerSelectorFactory consumerSelectorFactory ) {
    this.consumerSelectorFactory = consumerSelectorFactory;
  }

  @Override public <T> void registerOnlyForMessageClass( Class<T> messageClass, Consumer<T> consumer ) {
    MessageConsumerSelector selector = consumerSelectorFactory.createMessageClassOnlyConsumerSelector( messageClass );
    createSender( selector, consumer );
  }

  @Override public <T> void registerForMessageClassAndSubclasses( Class<T> messageClass, Consumer<T> consumer ) {
    MessageConsumerSelector selector = consumerSelectorFactory.createMessageClassAndSubclassesConsumerSelector(
      messageClass );
    createSender( selector, consumer );
  }

  private <T> void createSender( MessageConsumerSelector selector, Consumer<T> consumer ) {
    SendMessageIfMatches sender = new SendMessageIfMatches( selector, consumer );
    messageSenders.add( sender );
  }

  @Override public <T> void unregister( Class<T> messageClass, Consumer<T> consumer ) {
    new ArrayList<SendMessageIfMatches>( messageSenders ).stream()
      .filter( (sender) -> sender.matchesExactly( messageClass, consumer ) )
      .forEach( (sender) -> messageSenders.remove( sender ) );
  }

  @Override public <T> void removeAllConsumersForMessageClass( Class<T> messageClass ) {
    new ArrayList<SendMessageIfMatches>( messageSenders ).stream()
      .filter( (sender) -> sender.matchesForSending( messageClass ) )
      .forEach( (sender) -> messageSenders.remove( sender ) );
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

