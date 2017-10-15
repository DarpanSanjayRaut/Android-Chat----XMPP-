package com.pleximus.pet_app.application.builder.component;

import com.pleximus.pet_app.application.builder.module.ApiModule;
import com.pleximus.pet_app.application.builder.module.DatabaseModule;
import com.pleximus.pet_app.application.builder.module.RxModule;
import com.pleximus.pet_app.application.builder.scope.AppScope;
import com.pleximus.pet_app.core.api.APIInteface;
import com.pleximus.pet_app.core.bus.RxBus;
import com.pleximus.pet_app.core.db.DatabaseManager;

import dagger.Component;

/**
 * Created by pleximus on 21/04/17.
 */
@AppScope
@Component(modules = {ApiModule.class, RxModule.class, DatabaseModule.class}, dependencies = {NetworkComponent.class})
public interface ApiComponent {

    APIInteface provideApiInterface();

    RxBus rxBus();

    DatabaseManager databaseManager();

}
