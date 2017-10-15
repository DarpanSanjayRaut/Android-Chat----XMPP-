package com.pleximus.pet_app.core.bus.events;

/**
 * Created by pleximus on 06/05/17.
 */

public class RegisterAnyErrorEvent {

    public final String errorString;

    public RegisterAnyErrorEvent(String errorString) {
        this.errorString = errorString;
    }
}
