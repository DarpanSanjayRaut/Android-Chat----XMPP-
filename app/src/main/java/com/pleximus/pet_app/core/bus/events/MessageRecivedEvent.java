package com.pleximus.pet_app.core.bus.events;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by pleximus on 02/05/17.
 */

public class MessageRecivedEvent {

    public final Message message;

    public MessageRecivedEvent(Message message) {
        this.message = message;
    }
}
