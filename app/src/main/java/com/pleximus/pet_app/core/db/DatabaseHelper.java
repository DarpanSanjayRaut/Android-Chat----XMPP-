package com.pleximus.pet_app.core.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pleximus.pet_app.core.model.DBUser;
import com.pleximus.pet_app.core.model.DBUserContact;

/**
 * Created by pleximus on 25/04/17.
 */

public class
DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getName();

    // Database Name
    private static final String DATABASE_NAME = "PetApp.sqlite";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the User table
    private Dao<DBUser, Integer> dbUserDao = null;
    private Dao<DBUserContact, Integer> dbUserContactDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.v(TAG, "Creating Tables..");
            TableUtils.createTable(connectionSource, DBUser.class);
            TableUtils.createTable(connectionSource, DBUserContact.class);

        } catch (SQLException ex) {
            Log.e(TAG, "Can't create database\n" + ex.getMessage());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            Log.v(TAG, "Old Version: " + oldVersion + ", New Version: " + newVersion);
            database.execSQL("DROP TABLE DBUSer");
            database.execSQL("DROP TABLE DBUserContact");

        } catch (SQLException sqlException) {
            onCreate(database, connectionSource);
            Log.e(TAG, sqlException.getMessage());
        }
    }

    /**
     * get Database access object user Table
     *
     * @return
     */
    public Dao<DBUser, Integer> getDbUserDao() {
        if (null == dbUserDao) {
            try {
                dbUserDao = getDao(DBUser.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbUserDao;
    }

    /**
     * get Database access object user contact Table
     *
     * @return
     */
    public Dao<DBUserContact, Integer> getDbUserContactDao() {
        if (null == dbUserContactDao) {
            try {
                dbUserContactDao = getDao(DBUserContact.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbUserContactDao;
    }

    @Override
    public void close() {
        super.close();
        dbUserDao = null;
        dbUserContactDao = null;
    }
}
