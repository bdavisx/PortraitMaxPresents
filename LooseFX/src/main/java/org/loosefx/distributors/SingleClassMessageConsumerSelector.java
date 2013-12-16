package org.loosefx.distributors;

class SingleClassMessageConsumerSelector<T> extends AbstractMessageConsumerSelector<T>
  implements MessageConsumerSelector {

  public SingleClassMessageConsumerSelector( Class<T> messageClassToInclude ) {
    super( messageClassToInclude );
  }

  @Override public boolean test( Class messageClass ) {
    return getMessageClassToInclude().equals( messageClass );
  }

}
