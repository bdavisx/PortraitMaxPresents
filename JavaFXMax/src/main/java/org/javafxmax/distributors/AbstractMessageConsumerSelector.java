package org.javafxmax.distributors;

class AbstractMessageConsumerSelector<T> {
  private final Class<T> messageClassToInclude;

  public AbstractMessageConsumerSelector( Class<T> messageClassToInclude ) {
    this.messageClassToInclude = messageClassToInclude;
  }

  public boolean doesMessageClassMatchExactly( Class messageClass ) {
    return messageClassToInclude.equals( messageClass );
  }

  protected Class<T> getMessageClassToInclude() { return messageClassToInclude; }
}
