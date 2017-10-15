package com.pleximus.pet_app.core.bus.events;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by pleximus on 03/05/17.
 */

public class UserPresenceEvent {

    public final Presence userPresence;

    public UserPresenceEvent(Presence userPresence) {
        this.userPresence = userPresence;
    }
}
