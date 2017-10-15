package com.pleximus.pet_app.core.bus.events;

import com.pleximus.pet_app.core.model.DBUser;

/**
 * Created by pleximus on 05/05/17.
 */

public class RegisterNewUserEvent {

    public final DBUser dbUser;

    public RegisterNewUserEvent(DBUser dbUser) {
        this.dbUser = dbUser;
    }
}
