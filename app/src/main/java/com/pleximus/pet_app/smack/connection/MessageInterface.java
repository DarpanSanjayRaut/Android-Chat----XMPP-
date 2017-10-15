package com.pleximus.pet_app.smack.connection;

/**
 * Created by pleximus on 28/04/17.
 */

public interface MessageInterface {

    void onMessageRecived();
    void onMessageSent();
    void onError();

}
