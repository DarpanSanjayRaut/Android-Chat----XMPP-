package com.pleximus.pet_app.ui.login.core.presenter;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by pleximus on 21/04/17.
 */

public interface ILoginPresenter {

    void onLoad(Context context);

    void clear();

    void doLogin(Context context, String name, String passwd);

    void onSuccessfulLogin();

    void onLoadRegister();
}
