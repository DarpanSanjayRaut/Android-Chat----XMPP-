package com.pleximus.pet_app.core.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.pleximus.pet_app.utils.AppConstants;


/**
 * Created by pleximus on 15/03/17.
 */

public class SharedPrefs {

    private static final String PREFERENCE_NAME = "AndroidChat";
    private static final String KEY_LOGIN_STATUS = "LoginStatus";
    private static final String KEY_USERNAME = "UserName";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_ACTION_TYPE = "action";

    public static SharedPreferences sharedPreferences;

    /**
     * method to set the login status for the application
     *
     * @param context
     * @param status
     */
    public static void setLoginStatus(Context context, int status) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LOGIN_STATUS, status);
        editor.apply();
    }

    /**
     * method to get the status of login for the application
     *
     * @param context
     * @return login status
     */
    public static int getLoginStatus(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_LOGIN_STATUS, AppConstants.LOGIN_INITIALIZED);
    }

    /**
     * Method to store username and password of logged in user
     *
     * @param context
     * @param username
     * @param password
     */
    public static void setLoginUser(Context context, String username, String password) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    /**
     * Method to get Login Username
     *
     * @param context
     * @return
     */
    public static String getLoginUsername(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    /**
     * Method to get Login Password
     *
     * @param context
     * @return
     */
    public static String getLoginPassword(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    /**
     * Method to set action isSignIn or Login
     * @param context
     * @param isLogin
     */
    public static void setActionType(Context context, boolean isLogin) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ACTION_TYPE, isLogin);
        editor.apply();
    }


    /**
     * Method to get Action type
     * @param context
     * @return
     */
    public static boolean getActionType(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_ACTION_TYPE, true);
    }
}
