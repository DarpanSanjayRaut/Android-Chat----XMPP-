package com.pleximus.pet_app.ui.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.ContactsListEvent;
import com.pleximus.pet_app.core.bus.events.GetUserContactsEvent;
import com.pleximus.pet_app.core.bus.events.RegisterAnyErrorEvent;
import com.pleximus.pet_app.core.bus.events.UserAuthenticatedEvent;
import com.pleximus.pet_app.core.model.DBUserContact;
import com.pleximus.pet_app.smack.ui.ChatMessageActivity;
import com.pleximus.pet_app.ui.contacts.core.presenter.ContactsPresenterImpl;
import com.pleximus.pet_app.ui.contacts.core.presenter.IContactsPresenter;
import com.pleximus.pet_app.ui.contacts.core.view.IContactsView;
import com.pleximus.pet_app.ui.contacts.adapter.ContactsListAdapter;
import com.pleximus.pet_app.ui.contacts.dagger.component.DaggerContactsComponent;
import com.pleximus.pet_app.ui.contacts.dagger.module.ContactsModule;
import com.pleximus.pet_app.utils.AppConstants;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class ContactsListActivity extends AppCompatActivity implements IContactsView, ContactsListAdapter.ConactsListClickListner {

    private Context context;
    private ContactsListAdapter contactsListAdapter;

    @BindView(R.id.recycleview_contacts)
    RecyclerView contactsRecycleView;

    @Inject
    IContactsPresenter iContactsPresenter;
    @Inject
    ContactsPresenterImpl contactsPresenterImpl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        context = this;
        initaliseDagger();
        registerForContactsListEvent();
        registerForConnectionEvent();
        contactsPresenterImpl.onLoad();
    }

    /**
     * initialise dependencies
     */
    private void initaliseDagger() {
        DaggerContactsComponent.builder()
                .apiComponent(SignUpApplication.getAppComponent())
                .contactsModule(new ContactsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onContactsLoad(List<DBUserContact> dbUserContacts) {
        contactsRecycleView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        contactsRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        contactsRecycleView.setHasFixedSize(true);
        contactsRecycleView.setItemAnimator(new DefaultItemAnimator());
        contactsListAdapter = new ContactsListAdapter(context, this, dbUserContacts, getLayoutInflater());
        contactsRecycleView.setAdapter(contactsListAdapter);
    }

    @Override
    public void onError(String error) {
        //TODO error when recived
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    /**
     * register event
     */
    public void registerForContactsListEvent() {
        contactsPresenterImpl.addDisposableObserver(SignUpApplication.bus()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if (o instanceof ContactsListEvent) {
                            contactsPresenterImpl.onContactsLoad(((ContactsListEvent) o).dbUserContactList);
                        } else if (o instanceof RegisterAnyErrorEvent) {
                            contactsPresenterImpl.onError(((RegisterAnyErrorEvent) o).errorString);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("ContactsList > onError" + e.getMessage());
                        contactsPresenterImpl.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("ContactsList > onComplete");
                    }
                }));
    }

    /**
     * register for response for login
     */
    public void registerForConnectionEvent() {
        contactsPresenterImpl.addDisposableObserver(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof UserAuthenticatedEvent) {
                    Timber.i("User Logged In");
                    SignUpApplication.bus().send(new GetUserContactsEvent());
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.i("LoginResponse > onError" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Timber.i("LoginResponse > onComplete");
            }
        }));
    }

    @Override
    public void onContactClick(int position, DBUserContact contact) {
        Intent gotoChatScreen = new Intent(context, ChatMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.CONTACT_DETAILS, (Serializable) contact);
        gotoChatScreen.putExtras(bundle);
        startActivity(gotoChatScreen);
    }
}
