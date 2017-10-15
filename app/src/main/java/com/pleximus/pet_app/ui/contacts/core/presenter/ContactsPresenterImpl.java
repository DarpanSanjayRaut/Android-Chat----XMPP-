package com.pleximus.pet_app.ui.contacts.core.presenter;

import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.GetUserContactsEvent;
import com.pleximus.pet_app.core.model.DBUserContact;
import com.pleximus.pet_app.ui.base.BasePresenter;
import com.pleximus.pet_app.ui.contacts.core.view.IContactsView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by pleximus on 06/05/17.
 */

public class ContactsPresenterImpl extends BasePresenter implements IContactsPresenter {

    private IContactsView iContactsView;

    public ContactsPresenterImpl(IContactsView iContactsView) {
        this.iContactsView = iContactsView;
    }


    @Override
    public void onLoad() {
        // post event to get contacts list for the user
        SignUpApplication.bus().send(new GetUserContactsEvent());
    }

    @Override
    public void onContactsLoad(List<DBUserContact> dbUserContactsList) {
        iContactsView.onContactsLoad(dbUserContactsList);
    }

    @Override
    public void onError(String error) {
        iContactsView.onError(error);
    }

    /**
     * add disposable
     *
     * @param disposable
     */
    public void addDisposableObserver(Disposable disposable) {
        addDisposable(disposable);
    }
}
