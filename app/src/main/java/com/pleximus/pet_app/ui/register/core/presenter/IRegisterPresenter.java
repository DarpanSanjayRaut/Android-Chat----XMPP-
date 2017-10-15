package com.pleximus.pet_app.ui.register.core.presenter;

import android.content.Context;

/**
 * Created by pleximus on 04/05/17.
 */

public interface IRegisterPresenter {

    void onRegister(String firstname,
                    String lastname,
                    String nickname,
                    String password,
                    String emailId);

}
