package org.loosefx.distributors;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;

public class SingleClassMessageConsumerSelectorTest {
  @Test
  public void itShoudOnlyApproveBaseClass() throws Exception {
    MessageConsumerSelector consumerSelector = new SingleClassMessageConsumerSelector( TestEvents.EmptyBaseEvent.class );

    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();

    assertThat( consumerSelector.test( baseEvent.getClass() ) ).isTrue();
    assertThat( consumerSelector.test( inheritedEvent.getClass() ) ).isFalse();
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
