package com.pleximus.pet_app.core.bus.events;

/**
 * Created by pleximus on 04/05/17.
 */

public class MessageSendEvent {

    public final String messageBody;
    public final String messageTo;

    public MessageSendEvent(String messageBody, String messageTo) {
        this.messageBody = messageBody;
        this.messageTo = messageTo;
    }
}
