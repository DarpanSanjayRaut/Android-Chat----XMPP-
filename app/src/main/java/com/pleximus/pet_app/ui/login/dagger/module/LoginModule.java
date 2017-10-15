package com.pleximus.pet_app.ui.login.dagger.module;

import com.pleximus.pet_app.core.db.DatabaseManager;
import com.pleximus.pet_app.ui.login.LoginActivity;
import com.pleximus.pet_app.ui.login.core.model.UserModel;
import com.pleximus.pet_app.ui.login.core.presenter.ILoginPresenter;
import com.pleximus.pet_app.ui.login.core.presenter.LoginPresenterImp;
import com.pleximus.pet_app.ui.login.core.view.ILoginView;
import com.pleximus.pet_app.ui.login.dagger.scope.LoginScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pleximus on 21/04/17.
 */
@Module
public class LoginModule {

    private ILoginView iLoginView;

    public LoginModule(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    @LoginScope
    @Provides
    ILoginView providesILoginView() {
        return iLoginView;
    }

    @LoginScope
    @Provides
    ILoginPresenter providesLoginPresenter(ILoginView iLoginView) {
        return new LoginPresenterImp(iLoginView);
    }

    @LoginScope
    @Provides
    LoginPresenterImp providesLoginPresenterImpl(ILoginView iLoginView) {
        return new LoginPresenterImp(iLoginView);
    }

    @LoginScope
    @Provides
    UserModel providesUserModel(DatabaseManager databaseManager) {
        return new UserModel(databaseManager);
    }

}
