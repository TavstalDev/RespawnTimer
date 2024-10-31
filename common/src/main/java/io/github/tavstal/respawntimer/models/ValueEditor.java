package io.github.tavstal.respawntimer.models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ValueEditor {
    EFieldType type();
}
