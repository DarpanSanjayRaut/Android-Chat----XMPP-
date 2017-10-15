package com.pleximus.pet_app.core.bus.events;

/**
 * Created by pleximus on 05/05/17.
 */

public class ServerConnectedEvent {

    public final boolean isConnected;

    public ServerConnectedEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
