package org.loosefx.commands;

import org.loosefx.distributors.ConsumerSelectorFactory;
import org.loosefx.distributors.ObjectDistributor;
import org.loosefx.distributors.SimpleObjectDistributor;
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
    ObjectDistributor objectDistributor = mock( ObjectDistributor.class );
    CommandDistributor distributor = new CommandDistributor( objectDistributor );
    Consumer<TestCommand> consumer = handler::handle;
    distributor.register( TestCommand.class, consumer );
    distributor.unregister( TestCommand.class, consumer );

    verify( objectDistributor ).unregister( TestCommand.class, consumer );
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
    return new CommandDistributor( new SimpleObjectDistributor( consumerSelectorFactory ) );
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
