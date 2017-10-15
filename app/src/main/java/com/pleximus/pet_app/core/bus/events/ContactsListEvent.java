package com.pleximus.pet_app.core.bus.events;

import com.pleximus.pet_app.core.model.DBUserContact;

import java.util.List;

/**
 * Created by pleximus on 06/05/17.
 */

public class ContactsListEvent {
    public final List<DBUserContact> dbUserContactList;

    public ContactsListEvent(List<DBUserContact> dbUserContactList) {
        this.dbUserContactList = dbUserContactList;
    }
}
