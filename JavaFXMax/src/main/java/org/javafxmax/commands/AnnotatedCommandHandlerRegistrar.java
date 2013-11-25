package org.javafxmax.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.javafxmax.domain.commands.ApplicationCommandHandler;
import org.javafxmax.mvvm.guicommands.GUICommandHandler;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/** This class pretty much expects the annotated objects to be singletons in the injector. */
public class AnnotatedCommandHandlerRegistrar {
  private final Injector injector;
  private final CommandDistributor commandDistributor;
  private final Reflections reflections;

  @Inject
  public AnnotatedCommandHandlerRegistrar( Injector injector, CommandDistributor commandDistributor,
    Reflections reflections ) {
    this.injector = injector;
    this.commandDistributor = commandDistributor;
    this.reflections = reflections;
  }

  // TODO: figure out if we can hook into the creation of objects in the binder to connect

  public void registerAnnotatedHandlers() {
    Set<Method> methodsToRegister = findMethodsToRegister( reflections );
    registerMethods( methodsToRegister );
  }

  private Set<Method> findMethodsToRegister( Reflections reflections ) {
    Set<Method> methodsToRegister = new HashSet<>();
    methodsToRegister.addAll( findApplicationCommandHandlerMethods( reflections ) );
    methodsToRegister.addAll( findGUICommandHandlerMethods( reflections ) );
    return methodsToRegister;
  }

  private Set<Method> findApplicationCommandHandlerMethods( Reflections reflections ) {
    return reflections.getMethodsAnnotatedWith( ApplicationCommandHandler.class );
  }

  private Set<Method> findGUICommandHandlerMethods( Reflections reflections ) {
    return reflections.getMethodsAnnotatedWith( GUICommandHandler.class );
  }

  private void registerMethods( Set<Method> methodsToRegister ) {
    for( Method handlerMethod : methodsToRegister ) {
      registerMethod( handlerMethod );
    }
  }

  private void registerMethod( Method handlerMethod ) {
    Provider<?> provider = injector.getProvider( handlerMethod.getDeclaringClass() );
    Object handlerObject = provider.get();
    Class messageType = handlerMethod.getParameterTypes()[0];
    HandlerHolder holder = new HandlerHolder( handlerMethod, handlerObject );
    commandDistributor.register( messageType, holder.createConsumer() );
  }

  private class HandlerHolder {
    private final Method handlerMethod;
    private final Object handlerObject;

    public HandlerHolder( Method handlerMethod, Object handlerObject ) {
      this.handlerMethod = handlerMethod;
      this.handlerObject = handlerObject;
    }

    private Consumer createConsumer( ) {
      return new Consumer() {
        @Override public void accept( Object o ) {
          try {
            handlerMethod.invoke( handlerObject, o );
          } catch( IllegalAccessException | InvocationTargetException ex ) {
            throw new UnableToInvokeAutoRegisteredCommandHandlerException( handlerMethod, handlerObject, ex );
          }
        }
      };
    }
  }

}
