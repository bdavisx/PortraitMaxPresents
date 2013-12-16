package org.javafxmax.distributors;

import java.util.function.Consumer;

public interface ObjectDistributor {
  /** Register a consumer to receive notification of a particular message. */
  <T> void registerOnlyForMessageClass( Class<T> messageClass, Consumer<T> consumer );

  /** Register a consumer to receive notification of a particular message. */
  <T> void registerForMessageClassAndSubclasses( Class<T> messageClass, Consumer<T> consumer );

  /** Removes a consumer from receiving notifications. */
  <T> void unregister( Class<T> messageClass, Consumer<T> consumer );

  <T> boolean doesMessageClassHaveConsumers( Class<T> messageClass );

  /** Sends notifications to all registered consumers. */
  void send( Object message );

  /** WIll remove all exact match consumers. */
  <T> void removeAllConsumersForMessageClass( Class<T> messageClass );
}
