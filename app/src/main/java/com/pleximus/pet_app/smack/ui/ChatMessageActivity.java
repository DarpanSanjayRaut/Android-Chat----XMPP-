package com.pleximus.pet_app.smack.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.pleximus.pet_app.R;
import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.RxBus;
import com.pleximus.pet_app.core.bus.events.MessageRecivedEvent;
import com.pleximus.pet_app.core.bus.events.MessageSendEvent;
import com.pleximus.pet_app.core.model.DBUserContact;
import com.pleximus.pet_app.smack.ui.dagger.component.DaggerChatMessageComponent;
import com.pleximus.pet_app.utils.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ChatMessageActivity extends AppCompatActivity {

    private String contactJid;
    private Context context;
    private User me;
    private DBUserContact dbUserContact;

    @BindView(R.id.chat_view)
    ChatView mChatView;

    @Inject
    RxBus rxBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        ButterKnife.bind(this);
        context = this;
        initialiseDependencies();
        messageRecivedEvent();
        initialiseChatUI();

        dbUserContact = (DBUserContact)  getIntent().getExtras().getSerializable(AppConstants.CONTACT_DETAILS);

        // dummy me
        me = new User(0, "azhar", null);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(view -> {
            if (!mChatView.getInputText().isEmpty()) {
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                SignUpApplication.bus().send(new MessageSendEvent(mChatView.getInputText(), dbUserContact.getContactJID()));
                mChatView.setInputText("");
            } else {
                Toast.makeText(context, "Type something to send", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Method to initialise Dependencies
     */
    private void initialiseDependencies() {
        DaggerChatMessageComponent
                .builder()
                .apiComponent(SignUpApplication.getAppComponent())
                .build()
                .inject(this);
    }

    /**
     * Initialise Chat UI
     */
    private void initialiseChatUI() {
        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_bg));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(ContextCompat.getColor(this, R.color.chat_me));
        mChatView.setLeftMessageTextColor(ContextCompat.getColor(this, R.color.black));
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * register for message receiving event
     */
    private void messageRecivedEvent() {
        SignUpApplication.bus().toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof MessageRecivedEvent) {
                        org.jivesoftware.smack.packet.Message message = ((MessageRecivedEvent) o).message;
                        //Jid from = message.getFrom();
                        String from = message.getFrom().toString();
                        String contactJid1 = "";
                        if (from.contains("@")) {
                            contactJid1 = from.split("@")[0];
                        } else {
                            contactJid1 = from;
                        }
                        final User you = new User(1, contactJid1, null);
                        //Receive message
                        final Message receivedMessage = new Message.Builder()
                                .setUser(you)
                                .setRightMessage(false)
                                .setMessageText(message.getBody())
                                .build();
                        mChatView.receive(receivedMessage);
                        Timber.d("Message Received" + " > " + message.getBody());
                    }
                });
    }



}
