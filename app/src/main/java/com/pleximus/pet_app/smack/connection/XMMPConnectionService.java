package com.pleximus.pet_app.smack.connection;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

import timber.log.Timber;

public class XMMPConnectionService extends Service {
    private static final String TAG = "RoosterService";


    public static final String SEND_MESSAGE = "sendMessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";

    public static final String NEW_MESSAGE = "newMessage";
    public static final String BUNDLE_FROM_JID = "b_from";

    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;//We use this handler to post messages to
    //the background thread.
    public static ConnectionItem mConnection;


    public XMMPConnectionService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Initiaslise Connection to Server
     */
    private void initConnection() {
        if (mConnection == null) {
            mConnection = new ConnectionItem(this);
        }
        try {
            mConnection.createConnection();
        } catch (IOException | SmackException | XMPPException e) {
            Timber.i(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again");
            e.printStackTrace();
            //Stop the service all together.
            stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        if (!mActive) {
            mActive = true;
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(() -> {

                    Looper.prepare();
                    mTHandler = new Handler();
                    initConnection();
                    //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                    Looper.loop();

                });
                mThread.start();
            }
        }

    }

    public void stop() {
        mActive = false;
        mTHandler.post(() -> {
            if (mConnection != null) {
                mConnection.disconnect();
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return Service.START_STICKY;
        //RETURNING START_STICKY CAUSES OUR CODE TO STICK AROUND WHEN THE APP ACTIVITY HAS DIED.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}
