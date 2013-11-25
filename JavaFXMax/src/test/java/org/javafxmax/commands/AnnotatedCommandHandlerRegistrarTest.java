package org.javafxmax.commands;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.javafxmax.domain.commands.ApplicationCommand;
import org.javafxmax.domain.commands.ApplicationCommandHandler;
import org.javafxmax.mvvm.guicommands.AbstractCorrelatedGUICommand;
import org.javafxmax.mvvm.guicommands.GUICommandHandler;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class AnnotatedCommandHandlerRegistrarTest {

  // TODO: use this createReflections() in a guava module.
  /** This is the way to actually create a Reflections, although we'll use a mock for the test. */
  @Test
  public void applicationCommandAnnotation() {
    Injector injector = Guice.createInjector( new TestModule() );
    CommandDistributorRecorder commandDistributor =
      (CommandDistributorRecorder) injector.getInstance( CommandDistributor.class );

    AnnotatedCommandHandlerRegistrar registrar = injector.getInstance( AnnotatedCommandHandlerRegistrar.class );

    registrar.registerAnnotatedHandlers();

    assertTrue( commandDistributor.testCommandRegistered );
  }

  @Test( expected = UnableToInvokeAutoRegisteredCommandHandlerException.class )
  public void exeptionInHandler() {
    Injector injector = Guice.createInjector( new TestModule() );
    CommandDistributorRecorder distributor =
      (CommandDistributorRecorder) injector.getInstance( CommandDistributor.class );

    AnnotatedCommandHandlerRegistrar registrar = injector.getInstance( AnnotatedCommandHandlerRegistrar.class );

    registrar.registerAnnotatedHandlers();

    TestClassWithAnnotatedHandlers handler = injector.getInstance( TestClassWithAnnotatedHandlers.class );
    handler.shouldThrowException = true;

    TestApplicationCommand command = new TestApplicationCommand();
    distributor.send( command );
  }

  public static class TestModule extends AbstractModule {
    @Override protected void configure() {
      bind( CommandDistributor.class ).toInstance( new CommandDistributorRecorder() );
      bind( Reflections.class ).toInstance( createReflections() );
      bind( TestClassWithAnnotatedHandlers.class ).in( Singleton.class );
    }

    private Reflections createReflections() {
      return new Reflections( new ConfigurationBuilder()
        .addUrls( ClasspathHelper.forPackage( "org.javafxmax" ) )
        .addUrls( ClasspathHelper.forPackage( "com.portraitmax" ) )
        .setScanners( new MethodAnnotationsScanner() ) );
    }

  }

  public static class CommandDistributorRecorder extends CommandDistributor {
    public boolean testCommandRegistered;

    @Override public <T> void register( Class<T> messageClass, Consumer<T> consumer ) {
      if( messageClass.equals( TestApplicationCommand.class ) ) {
        testCommandRegistered = true;
      }
      super.register( messageClass, consumer );
    }
  }

  public static class TestClassWithAnnotatedHandlers {
    public boolean shouldThrowException = false;

    @ApplicationCommandHandler
    public void handleApplicationCommand( TestApplicationCommand command ) {
      if( shouldThrowException ) throw new RuntimeException( "Test" );
    }

    @GUICommandHandler
    public void handleGUICommand( TestGUICommand command ) {
      if( shouldThrowException ) throw new RuntimeException( "Test" );
    }
  }

  private static class TestApplicationCommand implements ApplicationCommand {
  }

  private static class TestGUICommand extends AbstractCorrelatedGUICommand {

    public TestGUICommand( UUID identifier ) {
      super( identifier );
    }
  }
}