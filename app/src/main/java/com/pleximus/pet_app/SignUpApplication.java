package com.pleximus.pet_app;

import android.app.Application;

import com.pleximus.pet_app.application.builder.component.ApiComponent;
import com.pleximus.pet_app.application.builder.component.DaggerApiComponent;
import com.pleximus.pet_app.application.builder.component.DaggerNetworkComponent;
import com.pleximus.pet_app.application.builder.component.NetworkComponent;
import com.pleximus.pet_app.application.builder.module.DatabaseModule;
import com.pleximus.pet_app.application.builder.module.NetworkModule;
import com.pleximus.pet_app.application.builder.module.RxModule;
import com.pleximus.pet_app.core.bus.RxBus;
import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.smack.connection.XMMPConnectionService;
import com.pleximus.pet_app.utils.AppConstants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by pleximus on 21/04/17.
 */

public class SignUpApplication extends Application {

    private static RxBus rxBus;
    private Context context;
    private static ApiComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        rxBus = new RxBus();
        initialiseLogger();
        resolveDependency();
    }

    /**
     * Method to resolve Application Scope Dependencies
     */
    private void resolveDependency() {
        if (appComponent == null) {
            appComponent = DaggerApiComponent.builder()
                    .networkComponent(getNetworkComponent())
                    .databaseModule(new DatabaseModule(this))
                    .rxModule(new RxModule())
                    .build();
        }
    }

    /**
     * Initialise Network Component
     *
     * @return
     */
    public NetworkComponent getNetworkComponent() {
        return DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(AppConstants.BASE_URL))
                .build();
    }

    /**
     * get AppComponent Instance
     *
     * @return
     */
    public static ApiComponent getAppComponent() {
        return appComponent;
    }

    /**
     * get Rxbus instance
     *
     * @return
     */
    public static RxBus bus() {
        if (rxBus == null) {
            return new RxBus();
        }
        return rxBus;
    }

    /**
     * Initialise Timber Log
     */
    private void initialiseLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });

        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    //TODO  decide what to log in release version
                }
            });
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            //FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    //FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    //FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
