package com.pleximus.pet_app.smack.connection.listner;


import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.MessageRecivedEvent;
import com.pleximus.pet_app.core.bus.events.ServerConnectedEvent;
import com.pleximus.pet_app.core.bus.events.UserAuthenticatedEvent;
import com.pleximus.pet_app.smack.connection.ConnectionConfig;
import com.pleximus.pet_app.smack.connection.ConnectionItem;
import com.pleximus.pet_app.smack.connection.ConnectionState;

import org.jivesoftware.smack.XMPPConnection;

import timber.log.Timber;

/**
 * Created by pleximus on 02/05/17.
 */

public class ConnectionListener implements org.jivesoftware.smack.ConnectionListener {


    private ConnectionItem connectionItem;

    public ConnectionListener(ConnectionItem connectionItem) {
        this.connectionItem = connectionItem;
    }

    @Override
    public void connected(XMPPConnection connection) {
        Timber.i("connected");
        connectionItem.updateState(ConnectionState.connected);
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Timber.i("authenticated");
        connectionItem.updateState(ConnectionState.authentication);
        if (connection.isAuthenticated()) {
            SignUpApplication.bus().send(new UserAuthenticatedEvent(ConnectionConfig.UI_AUTHENTICATED));
        }
    }

    @Override
    public void connectionClosed() {
        Timber.i("connectionClosed");
        connectionItem.updateState(ConnectionState.disconnecting);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Timber.i("connectionClosedOnError");
    }

    @Override
    public void reconnectionSuccessful() {
        Timber.i("reconnectionSuccessful");
        connectionItem.updateState(ConnectionState.connected);
    }

    @Override
    public void reconnectingIn(int seconds) {
        Timber.i("reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Timber.i("reconnectionFailed");
        connectionItem.updateState(ConnectionState.offline);
    }
}
