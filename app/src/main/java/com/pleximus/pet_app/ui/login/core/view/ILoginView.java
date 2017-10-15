package com.pleximus.pet_app.ui.login.core.view;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by pleximus on 21/04/17.
 */

public interface ILoginView {

    void onClearText();

    void onLoginResult(boolean isValid);

    void onLoginError();

    void startServerConnection(String username, String passwd);

    void onLoadRegisterActivity();
}
