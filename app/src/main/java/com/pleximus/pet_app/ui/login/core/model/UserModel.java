package com.pleximus.pet_app.ui.login.core.model;

import com.pleximus.pet_app.core.db.DatabaseManager;

public class UserModel implements IUser {

    private String name;
    private String passwd;
    private DatabaseManager dbManager;

    public UserModel(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public UserModel(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public UserModel() {
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public int checkUserValidity(String name, String passwd) {
        if (name == null || passwd == null || !name.equals(getUserName()) || !passwd.equals(getPassword())) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean userInputValidation(String name, String passwd) {
        if ((name != null && name.isEmpty()) || (passwd != null && passwd.isEmpty())) {
            return false;
        } else
            return true;
    }
}