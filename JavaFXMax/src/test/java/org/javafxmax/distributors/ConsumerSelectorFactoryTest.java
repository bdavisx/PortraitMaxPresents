package org.javafxmax.distributors;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;

public class ConsumerSelectorFactoryTest {
  @Test
  public void itShouldCreateASingleClassMessageConsumerSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector = factory.createMessageClassOnlyConsumerSelector( TestEvents.EmptyBaseEvent.class );

    assertThat( consumerSelector ).isOfAnyClassIn( SingleClassMessageConsumerSelector.class );
  }

  @Test
  public void itShouldCreateAIncludeSublcassesMessageConsumerSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector = factory.createMessageClassOnlyConsumerSelector( TestEvents.EmptyBaseEvent.class );

    assertThat( consumerSelector ).isOfAnyClassIn( IncludeSubclassesMessageConsumerSelector.class );
  }


  @Test
  public void itShouldCreateASingleClassSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector = factory.createMessageClassOnlyConsumerSelector( TestEvents.EmptyBaseEvent.class );

    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isFalse();
  }

  @Test
  public void itShouldCreateSubclassSelector() throws Exception {
    ConsumerSelectorFactory factory = new ConsumerSelectorFactory();
    MessageConsumerSelector consumerSelector =
      factory.createMessageClassAndSubclassesConsumerSelector( TestEvents.EmptyBaseEvent.class );

    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isTrue();
  }

}
