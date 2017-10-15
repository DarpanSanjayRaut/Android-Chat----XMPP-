package com.pleximus.pet_app.smack.connection.listner;

import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.UserPresenceEvent;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jivesoftware.smack.roster.RosterListener;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;

import java.util.Collection;

/**
 * Created by pleximus on 03/05/17.
 */

public class UserPresenceListener implements RosterListener {

    @Override
    public void entriesAdded(Collection<Jid> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {
        // User Presence Online/Offline
        SignUpApplication.bus().send(new UserPresenceEvent(presence));
    }
}
