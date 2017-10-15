package com.pleximus.pet_app.ui.register.core.presenter;

import android.content.Context;

import com.pleximus.pet_app.ui.base.BasePresenter;
import com.pleximus.pet_app.ui.register.core.model.RegisterModel;
import com.pleximus.pet_app.ui.register.core.view.IRegisterView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by pleximus on 04/05/17.
 */

public class RegisterPresenterImpl extends BasePresenter implements IRegisterPresenter {

    private List<String> errorList;
    RegisterModel registerModel;

    private IRegisterView iRegisterView;

    public RegisterPresenterImpl(IRegisterView iRegisterView, Context context) {
        this.iRegisterView = iRegisterView;
        registerModel = new RegisterModel(context);
    }

    @Override
    public void onRegister(String firstname, String lastname, String nickname, String password, String emailId) {
        errorList = registerModel.validateRegistertionInput(firstname, lastname, nickname, password, emailId);
        if (errorList != null && errorList.size() > 0) {
            iRegisterView.onError(errorList);
        } else {
            iRegisterView.onSuccessfulRegisteration(registerModel.getUserDetails(firstname, lastname, nickname, password, emailId));
        }
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
