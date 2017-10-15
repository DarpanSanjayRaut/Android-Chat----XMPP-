package com.pleximus.pet_app.smack;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.smack.connection.encryption.Encryption;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.jid.util.JidUtil;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class XMPPChatActivity extends AppCompatActivity{

    public static XMPPTCPConnection mConnection;
    public static XMPPTCPConnectionConfiguration.Builder builder;
    public static final String HOST = "10.0.0.126";
    public static final int PORT = 5222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmppchat);

        callXmppConnection("azhar@10.0.0.127");

    }

    private void callXmppConnection(String userId) {

        new XmppServerConncetion().execute(userId);
    }

    //For disconnect Chat
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
    }

    class XmppServerConncetion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                //This is for remove security for simple chat
                SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

                Jid jid;
                jid = JidCreate.from(HOST);

                builder = XMPPTCPConnectionConfiguration.builder();

                builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

                builder.setHost(HOST);

                //Global.builder.setResource("Phone");
                builder.setPort(PORT);

                builder.setDebuggerEnabled(true);

                builder.setXmppDomain((DomainBareJid) jid);

                mConnection = new XMPPTCPConnection(builder.build());
                mConnection.connect();
                //builder.performSaslAnonymousAuthentication();
                //connection.login();
                mConnection.login("azhar", "azhar");

                String name = mConnection.getServiceName().toString();

            } catch (SmackException | IOException | XMPPException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mConnection.isConnected()) {
                Log.d("connection", "connection successfully Done");

                ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                chatManager.addIncomingListener(new IncomingChatMessageListener() {
                    @Override
                    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                        Message message1 = message;
                    }
                });

                Jid jid = null;
                EntityBareJid eid = null;
                try {
                     // eid= JidCreate.from("azhar@",HOST,null).asEntityBareJidOrThrow();
                   eid = JidCreate.entityBareFrom("azhar@10.0.0.125/1ausy929wy");
                    // jid= JidCreate.from(username,"@ip-172-31-40-69",null);
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                Chat chat = chatManager.chatWith(eid);
                try {
                    //seanding message
                    chat.send("test");
                } catch (SmackException.NotConnectedException | InterruptedException e) {
                    e.printStackTrace();
                }

            }


//            try {
////                * <ul>
////                *      <li>name -- the user's name.
////                        *      <li>first -- the user's first name.
////                        *      <li>last -- the user's last name.
////                        *      <li>email -- the user's email address.
////                        *      <li>city -- the user's city.
////                        *      <li>state -- the user's state.
////                        *      <li>zip -- the user's ZIP code.
////                        *      <li>phone -- the user's phone number.
////                        *      <li>url -- the user's website.
////                        *      <li>date -- the date the registration took place.
////                *      <li>misc -- other miscellaneous information to associate with the account.
////                *      <li>text -- textual information to associate with the account.
////                        *      <li>remove -- empty flag to remove account.
//                Map<String, String> attributes = new HashMap<String, String>();
//                attributes.put("name", "nikname");
//                attributes.put("first", "darpan");
//                attributes.put("last", "raut");
//                attributes.put("email", "darpan@gmail.com");
//
//                VCard v = new VCard();
//
//                // Registering the user
//                AccountManager accountManager = AccountManager.getInstance(mConnection);
//                accountManager.sensitiveOperationOverInsecureConnection(true);
//                accountManager.createAccount(Localpart.from("darpan"), "darpan", attributes);   // Skipping optional fields like email, first name, last name, etc..
//            } catch (SmackException | IOException | XMPPException e) {
//                Log.e("TAG", e.getMessage());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if (mConnection.isAuthenticated()) {
                Log.d("Authenticate", "Authenticate successfully Done");
                Log.d("UserId", mConnection.getUser() + "");
                //createGroupChat();

            }
        }
    }

    public void createGroupChat() {
        // Create a MultiUserChat using a Connection for a room
// Get the MultiUserChatManager
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(mConnection);
        try {
            EntityBareJid jid = JidCreate.entityBareFrom("one@" + "conference." + HOST);

// Create a MultiUserChat using an XMPPConnection for a room
            MultiUserChat muc = manager.getMultiUserChat(jid);

// Prepare a list of owners of the new room
            Set<Jid> owners = JidUtil.jidSetFrom(new String[]{"newuser@" + HOST, "azhar@" + HOST});

// Create the room
            Resourcepart nickname = Resourcepart.from("mathan");
            muc.create(nickname).getConfigFormManager().setRoomOwners(owners).submitConfigurationForm();
            muc.join(Resourcepart.from("newuser@" + HOST));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (MultiUserChatException.MucAlreadyJoinedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (MultiUserChatException.MissingMucCreationAcknowledgeException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (MultiUserChatException.NotAMucServiceException e) {
            e.printStackTrace();
        } catch (MultiUserChatException.MucConfigurationNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
