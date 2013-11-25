package org.javafxmax.mvvm.guicommands;

import java.lang.annotation.*;

@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
@Documented
public @interface GUICommandHandler {
}
