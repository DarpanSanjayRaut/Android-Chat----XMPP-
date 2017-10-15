package com.pleximus.pet_app.ui.register.core.model;

import android.content.Context;

import java.util.List;

/**
 * Created by pleximus on 05/05/17.
 */

public interface IRegisterModel {

    String userInputValidation(String validateString, String whatTovalidate);

    List<String> validateRegistertionInput(String firstname, String lastname, String nickname, String password, String emailId);
}
