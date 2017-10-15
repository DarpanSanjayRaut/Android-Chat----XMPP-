package com.pleximus.pet_app.ui.contacts.dagger.module;

import com.pleximus.pet_app.ui.contacts.core.presenter.ContactsPresenterImpl;
import com.pleximus.pet_app.ui.contacts.core.presenter.IContactsPresenter;
import com.pleximus.pet_app.ui.contacts.core.view.IContactsView;
import com.pleximus.pet_app.ui.contacts.dagger.scope.ContactsScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pleximus on 06/05/17.
 */
@Module
public class ContactsModule {

    private IContactsView iContactsView;

    public ContactsModule(IContactsView iContactsView) {
        this.iContactsView = iContactsView;
    }

    @ContactsScope
    @Provides
    IContactsView providesContactView() {
        return iContactsView;
    }

    @ContactsScope
    @Provides
    IContactsPresenter providesContactsPresneter(IContactsView iContactsView) {
        return new ContactsPresenterImpl(iContactsView);
    }

    @ContactsScope
    @Provides
    ContactsPresenterImpl providesContactsPresenterImpl(IContactsView iContactsView) {
        return new ContactsPresenterImpl(iContactsView);
    }
}
