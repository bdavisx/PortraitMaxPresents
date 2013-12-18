package org.loosefx.distributors;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendMessageIfMatchesTest {
  @Test
  public void itShouldPassThruForMatchesExactly() throws Exception {
    MessageConsumerSelector selector = mock( MessageConsumerSelector.class );
    ConsumerEqualsChecker consumer = new ConsumerEqualsChecker();

    SendMessageIfMatches sender = new SendMessageIfMatches( selector, consumer );

    Class<TestEvents.EmptyBaseEvent> messageClass = TestEvents.EmptyBaseEvent.class;
    when( selector.doesMessageClassMatchExactly( messageClass ) ).thenReturn( true );
    sender.matchesExactly( messageClass, consumer );

    verify( selector ).doesMessageClassMatchExactly( messageClass );
    assertThat( consumer.equalsObject ).isSameAs( consumer );
  }

  public void handle( TestEvents.EmptyBaseEvent event ) {}

  private class ConsumerEqualsChecker implements Consumer<TestEvents.EmptyBaseEvent> {
    private Object equalsObject;

    @Override
    public void accept( final TestEvents.EmptyBaseEvent emptyBaseEvent ) {
    }

    @Override
    public boolean equals( final Object obj ) {
      equalsObject = obj;
      return super.equals( obj );
    }
  }
}
