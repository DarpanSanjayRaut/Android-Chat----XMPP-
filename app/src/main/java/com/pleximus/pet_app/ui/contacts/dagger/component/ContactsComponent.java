package com.pleximus.pet_app.ui.contacts.dagger.component;

import com.pleximus.pet_app.application.builder.component.ApiComponent;
import com.pleximus.pet_app.ui.contacts.ContactsListActivity;
import com.pleximus.pet_app.ui.contacts.dagger.module.ContactsModule;
import com.pleximus.pet_app.ui.contacts.dagger.scope.ContactsScope;

import dagger.Component;

/**
 * Created by pleximus on 06/05/17.
 */
@ContactsScope
@Component(modules = ContactsModule.class, dependencies = ApiComponent.class)
public interface ContactsComponent {
    void inject(ContactsListActivity contactsListActivity);
}
