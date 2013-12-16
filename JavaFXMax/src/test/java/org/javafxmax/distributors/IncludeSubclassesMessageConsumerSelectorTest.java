package org.javafxmax.distributors;

import org.javafxmax.distributors.MessageConsumerSelector;
import org.javafxmax.distributors.SingleClassMessageConsumerSelector;
import org.javafxmax.distributors.TestEvents;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class IncludeSubclassesMessageConsumerSelectorTest {
  @Test
  public void itShoudOnlyApproveAllInheritedClasses() throws Exception {
    MessageConsumerSelector consumerSelector = new IncludeSubclassesMessageConsumerSelector( TestEvents.EmptyBaseEvent.class );

    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isTrue();
  }

  @Test
  public void itShoudNotApproveBaseClassWhenInheritedClassIsType() throws Exception {
    MessageConsumerSelector consumerSelector =
      new SingleClassMessageConsumerSelector( TestEvents.EmptyInheritedEvent.class );

    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isFalse();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isTrue();
  }

}
