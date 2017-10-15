package com.pleximus.pet_app.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.GetUserContactsEvent;
import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.smack.connection.ConnectionItem;
import com.pleximus.pet_app.smack.connection.XMMPConnectionService;
import com.pleximus.pet_app.smack.ui.ChatMessageActivity;
import com.pleximus.pet_app.ui.contacts.ContactsListActivity;
import com.pleximus.pet_app.ui.login.LoginActivity;
import com.pleximus.pet_app.ui.splash.core.presenter.SplashScreenImpl;
import com.pleximus.pet_app.ui.splash.core.view.ISplashView;
import com.pleximus.pet_app.utils.AppConstants;
import com.pleximus.pet_app.utils.AppUtils;

public class SplashScreenActivity extends AppCompatActivity implements ISplashView {

    private SplashScreenImpl splashScreenImpl;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        splashScreenImpl = new SplashScreenImpl(this);
        splashScreenImpl.onActivityLoad();
    }

    @Override
    public void launchAppropriate() {
        new android.os.Handler().postDelayed(
                () -> {
                    if (SharedPrefs.getLoginStatus(context) == AppConstants.LOGIN_COMPLETED) {
                        if (!AppUtils.isMyServiceRunning(context, XMMPConnectionService.class)) {
                            Intent stop = new Intent(context, XMMPConnectionService.class);
                            context.startService(stop);
                        } else {
                            //TODO : how to restart service
//                            Intent stop = new Intent(context, XMMPConnectionService.class);
//                            context.stopService(stop);
//                            if (!AppUtils.isMyServiceRunning(context, XMMPConnectionService.class)) {
//                                Intent start = new Intent(context, XMMPConnectionService.class);
//                                context.startService(start);
//                            }
                        }
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setClass(context, ContactsListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setClass(context, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }, 2000);
    }
}
