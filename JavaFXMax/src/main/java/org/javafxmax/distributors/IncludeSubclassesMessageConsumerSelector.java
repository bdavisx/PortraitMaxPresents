package org.javafxmax.distributors;

class IncludeSubclassesMessageConsumerSelector<T>
  extends AbstractMessageConsumerSelector<T> implements MessageConsumerSelector {

  public IncludeSubclassesMessageConsumerSelector( Class<T> messageClassToInclude ) {
    super( messageClassToInclude );
  }

  @Override public boolean test( Class messageClass ) {
    return getMessageClassToInclude().isAssignableFrom( messageClass );
  }

}
