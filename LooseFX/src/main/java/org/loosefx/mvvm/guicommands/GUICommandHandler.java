package org.loosefx.mvvm.guicommands;

import java.lang.annotation.*;

@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
@Documented
public @interface GUICommandHandler {
}
