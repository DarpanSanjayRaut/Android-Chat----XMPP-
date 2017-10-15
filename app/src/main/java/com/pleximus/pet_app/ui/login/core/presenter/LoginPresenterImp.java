package com.pleximus.pet_app.ui.login.core.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.smack.connection.ConnectionItem;
import com.pleximus.pet_app.smack.connection.XMMPConnectionService;
import com.pleximus.pet_app.ui.base.BasePresenter;
import com.pleximus.pet_app.ui.login.core.model.IUser;
import com.pleximus.pet_app.ui.login.core.model.UserModel;
import com.pleximus.pet_app.ui.login.core.view.ILoginView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

import io.reactivex.disposables.Disposable;


/**
 * Created by pleximus on 21/04/17.
 */

public class LoginPresenterImp extends BasePresenter implements ILoginPresenter {

    private ILoginView iLoginView;
    private IUser user;

    public LoginPresenterImp(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        user = new UserModel();
    }

    @Override
    public void onLoad(Context context) {

    }

    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    @Override
    public void doLogin(Context context, String username, String passwd) {
        boolean isValidUser = user.userInputValidation(username, passwd);
        if (isValidUser) {
            iLoginView.startServerConnection(username, passwd);
        } else {
            iLoginView.onLoginError();
        }

    }

    @Override
    public void onSuccessfulLogin() {
        iLoginView.onLoginResult(true);
    }

    @Override
    public void onLoadRegister() {
        iLoginView.onLoadRegisterActivity();
    }


    /**
     * add disposable
     *
     * @param disposable
     */
    public void addDisposableObserver(Disposable disposable) {
        addDisposable(disposable);
    }

}
