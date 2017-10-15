package com.pleximus.pet_app.ui.contacts.core.presenter;

import com.pleximus.pet_app.core.model.DBUserContact;

import java.util.List;

/**
 * Created by pleximus on 06/05/17.
 */

public interface IContactsPresenter {

    void onLoad();

    void onContactsLoad(List<DBUserContact> dbUserContactsList);

    void onError(String error);
}
