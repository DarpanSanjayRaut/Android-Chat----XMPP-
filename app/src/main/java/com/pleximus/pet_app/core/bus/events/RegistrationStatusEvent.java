package com.pleximus.pet_app.core.bus.events;

/**
 * Created by pleximus on 05/05/17.
 */

public class RegistrationStatusEvent {

    public final boolean registerationSuccess;

    public RegistrationStatusEvent(boolean registerationSuccess) {
        this.registerationSuccess = registerationSuccess;
    }
}
