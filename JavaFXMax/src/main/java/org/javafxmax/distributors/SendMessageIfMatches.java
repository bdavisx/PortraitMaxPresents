package org.javafxmax.distributors;

import java.util.function.Consumer;

public class SendMessageIfMatches {
  private final MessageConsumerSelector consumerSelector;
  private final Consumer consumer;

  public SendMessageIfMatches( MessageConsumerSelector consumerSelector, Consumer consumer ) {
    this.consumer = consumer;
    this.consumerSelector = consumerSelector;
  }

  public boolean matchesForSending( Class messageClass ) {return consumerSelector.test( messageClass );}

  public boolean matchesExactly( Class messageClass, Consumer consumer ) {
    return consumerSelector.doesMessageClassMatchExactly( messageClass ) && this.consumer.equals( consumer );
  }

  public void sendMessage( Object message ) {
    if( consumerSelector.test( message.getClass() ) ) {
      consumer.accept( message );
    }
  }
}
