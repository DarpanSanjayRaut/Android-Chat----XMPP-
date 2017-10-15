package com.pleximus.pet_app.ui.register.core.view;

import com.pleximus.pet_app.core.model.DBUser;

import java.util.List;

/**
 * Created by pleximus on 04/05/17.
 */

public interface IRegisterView {

    void onClearText();

    void showRegisterProgress();

    void onSuccessfulRegisteration(DBUser dbUser);

    void onError(List<String> errorList);
}
