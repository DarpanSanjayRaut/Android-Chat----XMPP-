package com.pleximus.pet_app.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by pleximus on 06/05/17.
 */

public class AppUtils {

    /**
     * Check wheather service is already running
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
