package com.pleximus.pet_app.ui.login.core.model;

public interface IUser {

    String getUserName();

    String getPassword();

    int checkUserValidity(String name, String passwd);

    boolean userInputValidation(String name, String passwd);

}