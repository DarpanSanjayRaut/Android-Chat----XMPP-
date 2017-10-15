package com.pleximus.pet_app.ui.login.dagger.component;

import com.pleximus.pet_app.application.builder.component.ApiComponent;
import com.pleximus.pet_app.ui.login.LoginActivity;
import com.pleximus.pet_app.ui.login.core.model.UserModel;
import com.pleximus.pet_app.ui.login.dagger.module.LoginModule;
import com.pleximus.pet_app.ui.login.dagger.scope.LoginScope;

import dagger.Component;

/**
 * Created by pleximus on 21/04/17.
 */
@LoginScope
@Component(modules = {LoginModule.class, UserModel.class}, dependencies = ApiComponent.class)
public interface LoginComponent {

    void inject(LoginActivity loginActivity);
}
