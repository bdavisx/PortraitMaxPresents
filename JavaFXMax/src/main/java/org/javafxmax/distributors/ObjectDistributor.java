package org.javafxmax.distributors;

import java.util.function.Consumer;

public interface ObjectDistributor {
  /** Register a consumer to receive notification of a particular message. */
  <T> void register( Class<T> messageClass, Consumer<T> consumer );

  /** Removes a consumer from receiving notifications. */
  <T> void unregister( Class<T> messageClass, Consumer<T> consumer );

  <T> boolean doesMessageClassHaveConsumer( Class<T> messageClass );

  /** Sends notifications to all registered consumers. */
  void send( Object message );
}
