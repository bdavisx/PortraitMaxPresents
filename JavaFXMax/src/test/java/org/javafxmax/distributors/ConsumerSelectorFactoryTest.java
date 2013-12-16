package org.javafxmax.distributors;

import static  org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;

public class ConsumerSelectorFactoryTest {
  @Test
  public void itShouldCreateASingleClassSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector = factory.createMessageClassOnlyConsumerSelector( BaseEvent.class );

    BaseEvent baseEvent = new BaseEvent();
    InheritedEvent inheritedEvent = new InheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isFalse();
  }

  @Test
  public void itShouldCreateSubclassSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector =
      factory.createMessageClassAndSubclassesConsumerSelector( BaseEvent.class );

    BaseEvent baseEvent = new BaseEvent();
    InheritedEvent inheritedEvent = new InheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isTrue();
  }

  public class BaseEvent {}
  public class InheritedEvent extends BaseEvent {}
}
