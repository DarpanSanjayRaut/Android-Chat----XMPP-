package com.pleximus.pet_app.core.bus.events;

/**
 * Created by pleximus on 03/05/17.
 */

public class UserAuthenticatedEvent {

    public final String userAuthenticated;

    public UserAuthenticatedEvent(String userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }
}
