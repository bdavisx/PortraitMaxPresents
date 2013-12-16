package org.javafxmax.distributors;

class ConsumerSelectorFactory {
  public <T> MessageConsumerSelector createMessageClassOnlyConsumerSelector( Class<T> messageClass ) {
    return new SingleClassMessageConsumerSelector<T>( messageClass );
  }

  public <T> MessageConsumerSelector createMessageClassAndSubclassesConsumerSelector( Class<T> messageClass ) {
    return new IncludeSubclassesMessageConsumerSelector<T>( messageClass );
  }
}
