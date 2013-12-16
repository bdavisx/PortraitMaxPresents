package org.loosefx.distributors;

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
    MessageConsumerSelector consumerSelector = factory.createMessageClassAndSubclassesConsumerSelector(
      TestEvents.EmptyBaseEvent.class );

    assertThat( consumerSelector ).isOfAnyClassIn( IncludeSubclassesMessageConsumerSelector.class );
  }
}
