package com.pleximus.pet_app.ui.register.dagger.component;

import com.pleximus.pet_app.application.builder.component.ApiComponent;
import com.pleximus.pet_app.application.builder.component.NetworkComponent;
import com.pleximus.pet_app.ui.register.RegisterUserActivity;
import com.pleximus.pet_app.ui.register.core.model.RegisterModel;
import com.pleximus.pet_app.ui.register.dagger.module.RegisterModule;
import com.pleximus.pet_app.ui.register.dagger.scope.RegisterScope;

import dagger.Component;

/**
 * Created by pleximus on 04/05/17.
 */
@RegisterScope
@Component(modules = RegisterModule.class, dependencies = ApiComponent.class)
public interface RegisterComponent {

    void inject(RegisterUserActivity registerUserActivity);

}
