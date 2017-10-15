package com.pleximus.pet_app.ui.register.dagger.module;

import android.app.Activity;
import android.content.Context;

import com.pleximus.pet_app.ui.login.core.presenter.LoginPresenterImp;
import com.pleximus.pet_app.ui.register.core.model.IRegisterModel;
import com.pleximus.pet_app.ui.register.core.model.RegisterModel;
import com.pleximus.pet_app.ui.register.core.presenter.IRegisterPresenter;
import com.pleximus.pet_app.ui.register.core.presenter.RegisterPresenterImpl;
import com.pleximus.pet_app.ui.register.core.view.IRegisterView;
import com.pleximus.pet_app.ui.register.dagger.scope.RegisterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pleximus on 04/05/17.
 */
@Module
public class RegisterModule {

    private IRegisterView iRegisterView;
    private Activity activity;

    public RegisterModule(IRegisterView iRegisterView, Activity activity) {
        this.iRegisterView = iRegisterView;
        this.activity = activity;
    }

    @RegisterScope
    @Provides
    IRegisterView providesIRegisterView() {
        return iRegisterView;
    }

    @RegisterScope
    @Provides
    IRegisterPresenter providesRegisterPresenter(IRegisterView iRegisterView, Context context) {
        return new RegisterPresenterImpl(iRegisterView, context);
    }

    @RegisterScope
    @Provides
    RegisterPresenterImpl providesRegisterImpl(IRegisterView iRegisterView, Context context) {
        return new RegisterPresenterImpl(iRegisterView, context);
    }

    @RegisterScope
    @Provides
    Context providesActivityContext() {
        return activity;
    }

}
