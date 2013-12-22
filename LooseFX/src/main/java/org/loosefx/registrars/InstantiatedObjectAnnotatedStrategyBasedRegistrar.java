package org.loosefx.registrars;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.bushe.swing.event.EventSubscriber;
import org.loosefx.commands.UnableToInvokeAutoRegisteredCommandHandlerException;
import org.loosefx.domain.commands.ApplicationCommandHandler;
import org.loosefx.mvvm.guicommands.GUICommandHandler;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/*
  we can't really register for each individual domain object, either for commands
  or events; technically we could, like Axon does, but not sure if we need that functionality.
  For events though, the Domain object will just use the apply to itself (which should put
  the event in the container somewhere in that call stack - then the save can save the container -
  if we go that route).

  so for commands, unless we hook into instances instead of singletons, something is going to
  have to handle putting the commands (and possibly events, if the domain object is interested
  in another aggregates events (can they be).

  I guess it might be better to create some kind of router that works with a repository to distribute
  the commands / events to the correct aggregate as needed.

  //====================================================

  Thoughts, the application is going to need a reference to the open presentations (or whatever),
  and in this app anyway, I don't see any commands needing to be routed to a closed presentation. Therefore
  we should be able to find the correct object to route the commands to. But I dont' want to have to keep
  track of each individual object to send the commands to -- let's have an open aggregate cache that
  takes handles this automatically. This sort of takes out the need for the AnnotatedRegister class as
  far as aggregates go - but it could still be relevant for others.

*/

public class InstantiatedObjectAnnotatedStrategyBasedRegistrar {
  private final Class<? extends Annotation> annotationClass;

  @Inject
  public InstantiatedObjectAnnotatedStrategyBasedRegistrar( final Class<? extends Annotation> annotationClass ) {
    this.annotationClass = annotationClass;
  }

  // TODO: figure out if we can hook into the creation of objects in the binder to connect

  public void registerAnnotatedHandlers( Object objectToRegister ) {
    Set<Method> methodsToRegister = findMethodsToRegister( objectToRegister );
    registerMethods( methodsToRegister );
  }

  private Set<Method> findMethodsToRegister( Object objectToRegister ) {
    Set<Method> methodsToRegister = new HashSet<>();
    methodsToRegister.addAll( findAnnotatedMethods( objectToRegister ) );
    return methodsToRegister;
  }

  private Set<Method> findAnnotatedMethods( final Object objectToRegister ) {
    Reflections reflections = new Reflections( objectToRegister );
    return reflections.getMethodsAnnotatedWith( ApplicationCommandHandler.class );
  }

  private void registerMethods( Object objectToRegister, Set<Method> methodsToRegister ) {
    for( Method handlerMethod : methodsToRegister ) {
      registerMethod( objectToRegister, handlerMethod );
    }
  }

  private void registerMethod( final Object objectToRegister, Method handlerMethod ) {
    Provider<?> provider = injector.getProvider( handlerMethod.getDeclaringClass() );
    Object handlerObject = provider.get();
    Class messageType = handlerMethod.getParameterTypes()[0];
    HandlerHolder holder = new HandlerHolder( handlerMethod, handlerObject );
    commandDistributor.register( messageType, holder.createSubscriber() );
  }

  private class HandlerHolder {
    private final Method handlerMethod;
    private final Object handlerObject;

    public HandlerHolder( Method handlerMethod, Object handlerObject ) {
      this.handlerMethod = handlerMethod;
      this.handlerObject = handlerObject;
    }

    private EventSubscriber createSubscriber() {
      return new EventSubscriber() {
        @Override public void onEvent( Object o ) {
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
