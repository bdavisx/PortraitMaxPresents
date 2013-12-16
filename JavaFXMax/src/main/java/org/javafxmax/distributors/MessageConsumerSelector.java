package org.javafxmax.distributors;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface MessageConsumerSelector extends Predicate<Class> {
  boolean doesMessageClassMatchExactly( Class messageClass );
}
