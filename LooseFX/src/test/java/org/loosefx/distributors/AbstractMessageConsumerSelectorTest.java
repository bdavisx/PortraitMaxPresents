package org.loosefx.distributors;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractMessageConsumerSelectorTest {
  @Test
  public void itShouldMatchSameClasses() throws Exception {
    AbstractMessageConsumerSelector selector = new ConcreteAbstractMessageConsumerSelector(
      TestEvents.EmptyBaseEvent.class );

    assertThat( selector.doesMessageClassMatchExactly( TestEvents.EmptyBaseEvent.class ) ).isTrue();
  }

  @Test
  public void itShouldNotMatchSubClasses() throws Exception {
    AbstractMessageConsumerSelector selector = new ConcreteAbstractMessageConsumerSelector(
      TestEvents.EmptyBaseEvent.class );

    assertThat( selector.doesMessageClassMatchExactly( TestEvents.EmptyInheritedEvent.class ) ).isFalse();
  }


  private class ConcreteAbstractMessageConsumerSelector extends AbstractMessageConsumerSelector {
    public ConcreteAbstractMessageConsumerSelector( final Class messageClassToInclude ) {
      super( messageClassToInclude );
    }
  }
}
