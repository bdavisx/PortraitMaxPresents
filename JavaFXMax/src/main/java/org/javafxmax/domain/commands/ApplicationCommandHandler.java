package org.javafxmax.domain.commands;

import java.lang.annotation.*;

@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
@Documented
public @interface ApplicationCommandHandler {
}
