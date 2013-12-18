package org.loosefx.commands;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.ThreadSafeEventService;
import org.loosefx.distributors.ConsumerSelectorFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class CommandDistributorTest {

  @Test
  public void itShouldSendACommandToRegisteredHandler() throws Exception {
    TestHandler handler = new TestHandler();
    handler.receivedCommand = null;

    CommandDistributor distributor = createDistributorForTests();
    distributor.register( TestCommand.class, handler::handle );

    TestCommand command = new TestCommand();
    distributor.send( command );

    Assert.assertSame( command, handler.receivedCommand );
  }

  @Test
  public void itShouldPassThruUnregister() throws Exception {
    TestHandler handler = new TestHandler();
    handler.receivedCommand = null;

    ConsumerSelectorFactory consumerSelectorFactory = new ConsumerSelectorFactory();
    EventService objectDistributor = mock( EventService.class );
    CommandDistributor distributor = new CommandDistributor( objectDistributor );
    EventSubscriber<TestCommand> consumer = handler::handle;
    distributor.register( TestCommand.class, consumer );
    distributor.unregister( TestCommand.class, consumer );

    verify( objectDistributor ).unsubscribe( TestCommand.class, consumer );
  }

  @Test
  public void itShouldNotSendACommandToHandlerRegisteredForADifferentCommand() throws Exception {
    TestHandler handler = new TestHandler();
    handler.receivedCommand = null;
    TestHandler2 handler2 = new TestHandler2();
    handler2.receivedCommand = null;

    CommandDistributor distributor = createDistributorForTests();
    distributor.register( TestCommand.class, handler::handle );
    distributor.register( TestCommand2.class, handler2::handle );

    TestCommand2 command = new TestCommand2();
    distributor.send( command );

    Assert.assertNull( handler.receivedCommand );
    Assert.assertSame( command, handler2.receivedCommand );
  }

  @Test( expected = NoHandlerForCommandException.class )
  public void itShouldThrowAnExceptionIfNoHandlerRegistered() throws Exception {
    TestHandler handler = new TestHandler();
    handler.receivedCommand = null;
    TestHandler2 handler2 = new TestHandler2();
    handler2.receivedCommand = null;

    CommandDistributor distributor = createDistributorForTests();
    distributor.register( TestCommand.class, handler::handle );
    distributor.register( TestCommand2.class, handler2::handle );

    TestCommand3 command = new TestCommand3();
    distributor.send( command );
  }

  private CommandDistributor createDistributorForTests() {
    ConsumerSelectorFactory consumerSelectorFactory = new ConsumerSelectorFactory();
    return new CommandDistributor( new ThreadSafeEventService() );
  }

  private class TestCommand {}

  private class TestHandler {
    public TestCommand receivedCommand;

    public void handle( TestCommand command ) {
      receivedCommand = command;
    }
  }

  private class TestCommand2 {}

  private class TestHandler2 {
    public TestCommand2 receivedCommand;

    public void handle( TestCommand2 command ) {
      receivedCommand = command;
    }
  }

  private class TestCommand3 {}
}
