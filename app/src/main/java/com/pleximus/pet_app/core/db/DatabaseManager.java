package com.pleximus.pet_app.core.db;

import android.content.Context;

import com.pleximus.pet_app.core.model.DBUser;
import com.pleximus.pet_app.core.model.DBUserContact;

import java.sql.SQLException;


/**
 * Created by pleximus on 25/04/17.
 */

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getName();

    private static DatabaseManager dbManagerInstance;


    DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * returns the instance of the class
     *
     * @param context
     * @return instance
     */
    static public DatabaseManager getInstance(Context context) {
        if (dbManagerInstance == null) {
            dbManagerInstance = new DatabaseManager(context);
        }
        return dbManagerInstance;
    }

    /**
     * returns the Helper instance
     *
     * @return
     */
    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    /** User **/

    /**
     * add or update user details in database
     *
     * @param user
     */
    public boolean addUser(DBUser user) {
        if (user == null) {
            return false;
        } else {
            try {
                getHelper().getDbUserDao().createOrUpdate(user);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * add user contact details in database
     *
     * @param dbUserContact
     * @return
     */
    public boolean addUserContact(DBUserContact dbUserContact) {
        if (dbUserContact == null) {
            return false;
        } else {
            try {
                getHelper().getDbUserContactDao().createOrUpdate(dbUserContact);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
