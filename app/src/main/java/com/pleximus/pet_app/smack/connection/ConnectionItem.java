package com.pleximus.pet_app.smack.connection;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.ContactsListEvent;
import com.pleximus.pet_app.core.bus.events.GetUserContactsEvent;
import com.pleximus.pet_app.core.bus.events.MessageRecivedEvent;
import com.pleximus.pet_app.core.bus.events.MessageSendEvent;
import com.pleximus.pet_app.core.bus.events.RegisterAnyErrorEvent;
import com.pleximus.pet_app.core.bus.events.RegisterNewUserEvent;
import com.pleximus.pet_app.core.bus.events.RegistrationStatusEvent;
import com.pleximus.pet_app.core.bus.events.ServerConnectedEvent;
import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.core.model.DBUser;
import com.pleximus.pet_app.core.model.DBUserContact;
import com.pleximus.pet_app.smack.connection.listner.ConnectionListener;
import com.pleximus.pet_app.smack.connection.listner.UserPresenceListener;
import com.pleximus.pet_app.utils.AppConstants;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.jid.util.JidUtil;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

import static com.pleximus.pet_app.smack.connection.ConnectionConfig.HOST;

public class ConnectionItem {

    @Nullable
    private ConnectionListener connectionListener;

    /**
     * Current state.
     */
    private ConnectionState state;

    private Roster roster;

    /**
     * Listeners
     */
    private SubscribeListener rosterRecivedInviteSubscription;
    private RosterListener userPresnceListener;
    private IncomingChatMessageListener messageListener;
    private ReceiptReceivedListener receiptReceivedListener;

    public static XMPPTCPConnection connection;
    public static XMPPTCPConnectionConfiguration.Builder builder;

    private CompositeDisposable compositeDisposable;

    private final Context mApplicationContext;
    private final String mUsername;
    private final String mPassword;
    private final int isLoggedIn;


    private ChatManager chatManager;
    private AccountManager accountManager;
    private MultiUserChatManager multiUserChatManager;
    private DeliveryReceiptManager deliveryReceiptManager;
    private MultiUserChat multiUserChat;

    private String mServiceName;
    private Object contactOnlyName;

    public ConnectionItem(Context context) {
        configureSubscription();
        mApplicationContext = context.getApplicationContext();
        mUsername = SharedPrefs.getLoginUsername(mApplicationContext);
        mPassword = SharedPrefs.getLoginPassword(mApplicationContext);
        isLoggedIn = SharedPrefs.getLoginStatus(context);
    }

    /**
     * Setup Connection to XMMPServer
     *
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     * @throws InterruptedException
     */
    public void createConnection() throws IOException, XMPPException, SmackException, InterruptedException {
        Timber.log(0, "Connecting to Server...");
        try {
            //This is for remove security for simple chat
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            Jid jid;
            jid = JidCreate.from(ConnectionConfig.SERVICE_NAME);
            builder = XMPPTCPConnectionConfiguration.builder();
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            builder.setHost(HOST);
            builder.setResource(ConnectionConfig.CONNECTION_SOURCE);
            builder.setPort(ConnectionConfig.PORT);
            builder.setDebuggerEnabled(true);
            builder.setXmppDomain(jid.asDomainBareJid());
            connection = new XMPPTCPConnection(builder.build());
            initConnectionListener();
            addConnectionListeners();
            connection.connect();

            registerNewUserEvent();
            registerForGetContactsEvent();

            //builder.performSaslAnonymousAuthentication();
            //connection.login();
            if (SharedPrefs.getLoginStatus(mApplicationContext) != AppConstants.REGISTRATION) {
                if (mUsername != null && mPassword != null) {
                    connection.login(mUsername, mPassword);
                    connection.sendStanza(new Presence(Presence.Type.available));
                } else {
                    //TODO post some error message
                }
            } else {
                SignUpApplication.bus().send(new ServerConnectedEvent(true));
            }

            initialiseAllListeners();
            addRosterListeners();
            registerSendMessageEvent();
            handleOfflineMessages();

            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.setEnabledPerDefault(true);
            reconnectionManager.enableAutomaticReconnection();

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * register new user event
     */
    private void registerNewUserEvent() {
        addDisposable(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof RegisterNewUserEvent) {
                    DBUser dbUser = ((RegisterNewUserEvent) o).dbUser;
                    registerNewUser(dbUser.getuFirstName(), dbUser.getuLastName(), dbUser.getuPassWord(), dbUser.getuNickName(), dbUser.getuEmail());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    /**
     * register for sending message to user
     */
    private void registerSendMessageEvent() {
        addDisposable(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof MessageSendEvent) {
                    try {
                        sendMessage(((MessageSendEvent) o).messageBody, ((MessageSendEvent) o).messageTo);
                    } catch (InterruptedException | XMPPException | SmackException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }


    /**
     * register for get contacts event
     */
    private void registerForGetContactsEvent() {
        addDisposable(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof GetUserContactsEvent) {
                    getContactsForUser();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    /**
     * Send Message to Chat User
     *
     * @param body
     * @param toJid
     */
    private void sendMessage(String body, String toJid) throws InterruptedException, XMPPException, SmackException, IOException {

        EntityBareJid eID = null;
        try {
            //jid= JidCreate.from("username",HOST,null);
            eID = JidCreate.entityBareFrom(getContactOnlyName(toJid) + "@" + getServiceName());
            org.jivesoftware.smack.chat2.Chat chat = getChatManagerInstance().chatWith(eID);
            try {
                //sending message
                chat.send(body);
            } catch (SmackException.NotConnectedException e) {
                removeListeners();
                createConnection();
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    private void getDeliveryStatusOfMsesage() {

    }

    /**
     * Disconnecting from Server
     */
    public void disconnect() {
        Timber.i("Disconnecting from Server...");
        unSubscribeAll();
        ConnectionState state = getState();
        if (state.isConnected()) {
            removeListeners();
            getConnection().disconnect();
        }
        connection = null;
    }

    /**
     * update state of Connection
     */
    public void updateState(ConnectionState newState) {
        boolean changed = setState(newState);
    }

    /**
     * get connection state
     *
     * @return
     */
    public synchronized ConnectionState getState() {
        return state;
    }

    /**
     * set connection state
     *
     * @param newState
     * @return
     */
    private synchronized boolean setState(ConnectionState newState) {
        ConnectionState prevState = this.state;
        this.state = newState;
        Timber.i("updateState. prev " + prevState + " new " + newState);
        return prevState != state;
    }

    /**
     * get connection instance
     *
     * @return
     */
    public static XMPPTCPConnection getConnection() {
        return connection;
    }

    /**
     * add connection listeners
     */
    private void addConnectionListeners() {
        getConnection().addConnectionListener(connectionListener);
    }

    /**
     * add all Listeners
     */
    private void addRosterListeners() {
        getRosterInstance().addSubscribeListener(rosterRecivedInviteSubscription);
        getRosterInstance().addRosterListener(userPresnceListener);
        getChatManagerInstance().addIncomingListener(messageListener);
        addMesgDeliveryStatusListener();
        getDeliveryReceiptManager().addReceiptReceivedListener(receiptReceivedListener);
    }

    /**
     * remove connection listeners
     */
    private void removeListeners() {
        getConnection().removeConnectionListener(connectionListener);
        getRosterInstance().removeSubscribeListener(rosterRecivedInviteSubscription);
        getRosterInstance().removeRosterListener(userPresnceListener);
        getChatManagerInstance().removeListener(messageListener);
        getDeliveryReceiptManager().removeReceiptReceivedListener(receiptReceivedListener);
    }

    /**
     * initalise all required listeners
     */
    private void initialiseAllListeners() {
        // Roster //TODO : verify the below how to accept/reject request
        rosterRecivedInviteSubscription = (from, subscribeRequest) -> SubscribeListener.SubscribeAnswer.Approve;
        // User Presence
        userPresnceListener = new UserPresenceListener();
        // Incoming message
        messageListener = (from, message, chat) -> SignUpApplication.bus().send(new MessageRecivedEvent(message));
        // Message Delivery Status
        receiptReceivedListener = (fromJid, toJid, receiptId, receipt) -> {
            //TODO : on recieved status of message delivery
        };
    }

    /**
     * add message delivery status listener
     */
    private void addMesgDeliveryStatusListener() {
        getDeliveryReceiptManager().setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
        getDeliveryReceiptManager().autoAddDeliveryReceiptRequests();

    }

    /**
     * initalise connection listener
     */
    private void initConnectionListener() {
        // Connection
        connectionListener = new ConnectionListener(this);
    }

    /**
     * get roster instance
     *
     * @return
     */
    private Roster getRosterInstance() {
        if (roster == null && getConnection() != null) {
            roster = Roster.getInstanceFor(getConnection());
            if (!roster.isLoaded())
                try {
                    roster.reloadAndWait();
                } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return roster;
    }

    /**
     * get account manager instance
     *
     * @return
     */
    private AccountManager getAccountManagerInstance() {
        if (getConnection() != null) {
            accountManager = AccountManager.getInstance(getConnection());
        }
        return accountManager;
    }

    /**
     * get ChatManager Instance
     *
     * @return
     */
    private ChatManager getChatManagerInstance() {
        if (chatManager == null && getConnection() != null) {
            chatManager = ChatManager.getInstanceFor(getConnection());
        }
        return chatManager;
    }

    /**
     * get DeliveryReceiptManager
     *
     * @return
     */
    private DeliveryReceiptManager getDeliveryReceiptManager() {
        if (deliveryReceiptManager == null && getConnection() != null) {
            deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(getConnection());
        }
        return deliveryReceiptManager;
    }

    /**
     * get MultiChat Instance
     *
     * @return
     */
    private MultiUserChatManager getMultiUserChatManager() {
        if (multiUserChatManager == null && getConnection() != null) {
            multiUserChatManager = MultiUserChatManager.getInstanceFor(getConnection());
        }
        return multiUserChatManager;
    }

    /**
     * get Service Name
     *
     * @return
     */
    private String getServiceName() {
        if (getConnection() != null) {
            mServiceName = getConnection().getXMPPServiceDomain().toString();
        }
        return mServiceName;
    }


    /**
     * Initialise CompositeSubscription
     *
     * @return
     */
    private CompositeDisposable configureSubscription() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    /**
     * Method to UnSubscribe from all the subscriptions
     */
    protected void unSubscribeAll() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
            // mCompositeSubscription = null;
        }
    }

    /**
     * Method to add Observables to Disposable
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        configureSubscription().add(disposable);
    }

    /**
     * SEND REQUEST TO USER
     * <p>
     * Creates a new roster entry and presence subscription. The server will asynchronously
     * update the roster with the subscription status.
     *
     * @param username the user. (e.g. johndoe@jabber.org)
     * @param nickname the nickname of the user.
     * @param groups   the list of group names the entry will belong to, or <tt>null</tt> if the
     *                 the roster entry won't belong to a group.
     */
    private void addUserToRoster(String username, String nickname, String[] groups) {
        BareJid bareJid = null;
        try {
            bareJid = JidCreate.from(username + "@" + HOST).asBareJid();
            getRosterInstance().createEntry(bareJid, nickname, null);
        } catch (XmppStringprepException | SmackException.NoResponseException | SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NotLoggedInException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Send Invitation to user
     *
     * @param username
     */
    private void sentInvitationToUser(String username) {
        // send request to user
        BareJid bareJid = null;
        try {
            bareJid = JidCreate.from(username + "@" + HOST).asBareJid();
            getRosterInstance().sendSubscriptionRequest(bareJid);
        } catch (XmppStringprepException | SmackException.NotConnectedException | SmackException.NotLoggedInException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * **********  REGISTER NEW USER **************
     * * <ul>
     * <li>name -- the user's name.
     * <li>first -- the user's first name.
     * <li>last -- the user's last name.
     * <li>email -- the user's email address.
     * <li>city -- the user's city.
     * <li>state -- the user's state.
     * <li>zip -- the user's ZIP code.
     * <li>phone -- the user's phone number.
     * <li>url -- the user's website.
     * <li>date -- the date the registration took place.
     * <li>misc -- other miscellaneous information to associate with the account.
     * <li>text -- textual information to associate with the account.
     * <li>remove -- empty flag to remove account.
     */
    private void registerNewUser(String newUserName, String lastname, String password, String nickName, String emailId) {
        try {

            Map<String, String> attributes = new HashMap<String, String>();

            attributes.put("name", nickName);
            attributes.put("first", newUserName);
            attributes.put("last", lastname);
            attributes.put("email", emailId);

            // Registering the user
            getAccountManagerInstance().sensitiveOperationOverInsecureConnection(true);
            getAccountManagerInstance().createAccount(Localpart.from(newUserName), password, attributes);
            connection.login(newUserName, password);
            SignUpApplication.bus().send(new RegistrationStatusEvent(true));
        } catch (SmackException | IOException | XMPPException e) {
            Intent myService = new Intent(mApplicationContext, XMMPConnectionService.class);
            mApplicationContext.stopService(myService);
            SignUpApplication.bus().send(new RegistrationStatusEvent(false));
            Log.e("TAG", e.getMessage());
        } catch (InterruptedException e) {
            SignUpApplication.bus().send(new RegistrationStatusEvent(false));
            e.printStackTrace();
        }
    }

    /**
     * check user availablity
     *
     * @param username
     * @return
     * @throws XMPPException
     * @throws SmackException.NotConnectedException
     * @throws InterruptedException
     * @throws SmackException.NoResponseException
     */
    public Boolean checkIfUserExists(String username) throws XMPPException, SmackException.NotConnectedException, InterruptedException, SmackException.NoResponseException {
        boolean doesExists = false;
        try {
            UserSearchManager userSearchManager = new UserSearchManager(getConnection());
            DomainBareJid searchService = null;
            try {
                searchService = JidCreate.domainBareFrom(getServiceName());
                String newSearch = "search." + searchService;
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
            Form searchForm = userSearchManager.getSearchForm(searchService);
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", username);
            ReportedData data = userSearchManager.getSearchResults(answerForm, searchService);
            if (data.getRows() != null) {
                List<ReportedData.Row> rows = data.getRows();
                Iterator<ReportedData.Row> it = rows.iterator();
                if (it.hasNext()) {
                    //user exists
                    doesExists = true;
                } else {
                    //user doesnt exists
                    doesExists = false;
                }
            }
        } catch (Exception e) {
            e.getCause();
        }
        return doesExists;
    }

    /**
     * get contacts for loggedIn user
     */
    private void getContactsForUser() {
        List<DBUserContact> dbUserContactsList = new ArrayList<>();
        try {
            Collection<RosterEntry> entries = getRosterInstance().getEntries();
            for (RosterEntry entry : entries) {
                DBUserContact dbUserContact;
                Jid jidUser = null;
                try {
                    jidUser = JidCreate.from(entry.getJid());
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                BareJid bareId = (BareJid) jidUser;
                Presence presence = getRosterInstance().getPresence(bareId);
                String status = presence.getStatus();
                Presence.Type type = presence.getType();
                dbUserContact = new DBUserContact(jidUser.toString(), entry.getName(), status, type.toString());
                dbUserContactsList.add(dbUserContact);
            }
            if (dbUserContactsList != null && dbUserContactsList.size() > 0) {
                SignUpApplication.bus().send(new ContactsListEvent(dbUserContactsList));
            }
        } catch (Exception e) {
            SignUpApplication.bus().send(new RegisterAnyErrorEvent(e.toString()));
        }

    }

    public Object getContactOnlyName(String contact) {
        String contactJid = "";
        if (contact.contains("@")) {
            contactJid = contact.split("@")[0];
        } else {
            contactJid = contact;
        }
        return contactJid;
    }




    /*
    *
    *
    *   GROUP CHAT
    *
    * */

    public static void handleOfflineMessages()throws Exception {
        OfflineMessageManager offlineMessageManager = new OfflineMessageManager(getConnection());

        if (!offlineMessageManager.supportsFlexibleRetrieval()) {
            return;
        }

        if (offlineMessageManager.getMessageCount() == 0) {
            String d = "ss";
        } else {
            List<Message> msgs = offlineMessageManager.getMessages();
            for (Message msg : msgs) {
                BareJid fullJid = msg.getFrom().asBareJid();
                String messageBody = msg.getBody();
                if (messageBody != null) {

                }
            }
            offlineMessageManager.deleteMessages();
        }
    }

    private void createGroup(String groupName, List<String> groupUserList) {
        try {
            if (multiUserChat != null) {
                EntityBareJid currentGroupName = multiUserChat.getRoom();
                if (currentGroupName != null && currentGroupName.toString().equalsIgnoreCase(groupName)) {
                    multiUserChat.sendMessage("Ss");
                }
            }
            EntityBareJid jid = JidCreate.entityBareFrom(groupName + "conference." + HOST);
            multiUserChat = getMultiUserChatManager().getMultiUserChat(jid);

            Set<Jid> owners = JidUtil.jidSetFrom(groupUserList);
            Resourcepart nickname = Resourcepart.from("mathan");
            multiUserChat.create(nickname).getConfigFormManager().setRoomOwners(owners).submitConfigurationForm();
            multiUserChat.join(Resourcepart.from("newuser@" + HOST));




            List<EntityFullJid> groupusers = multiUserChat.getOccupants();

        } catch (Exception e) {
            e.printStackTrace();
            SignUpApplication.bus().send(new RegisterAnyErrorEvent(e.getMessage()));
        }
    }


    /**
     * method to get information for a group
     *
     * @param groupName
     * @throws XmppStringprepException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NotConnectedException
     * @throws InterruptedException
     * @throws SmackException.NoResponseException
     */
    private void getGroupInfo(String groupName) {
        try {
            String groupInfoName = groupName + "conference." + HOST;
            EntityBareJid entityBareJid = JidCreate.entityBareFrom(groupInfoName).asEntityBareJid();
            RoomInfo info = getMultiUserChatManager().getRoomInfo(entityBareJid);
            //group information
            int userCount = info.getOccupantsCount();
            String subject = info.getSubject();
        } catch (Exception e) {
            SignUpApplication.bus().send(new RegisterAnyErrorEvent(e.getMessage()));
        }
    }


    /**
     * get List of joined groups for a User
     *
     * @param userName
     */
    private void getJoinedGroupDetails(String userName) {
        try {
            EntityJid entityJid = JidCreate.from(userName + "@" + HOST).asEntityJidOrThrow();
            List<EntityBareJid> joinedRooms = getMultiUserChatManager().getJoinedRooms(entityJid);
            if (joinedRooms != null && joinedRooms.size() > 0) {
                //TODO : post the list to the activity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
