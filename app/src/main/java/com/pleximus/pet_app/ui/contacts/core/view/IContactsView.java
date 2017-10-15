package com.pleximus.pet_app.ui.contacts.core.view;

import com.pleximus.pet_app.core.model.DBUserContact;

import java.util.List;

/**
 * Created by pleximus on 06/05/17.
 */

public interface IContactsView {

    void onContactsLoad(List<DBUserContact> dbUserContacts);
    void onError(String error);
}
