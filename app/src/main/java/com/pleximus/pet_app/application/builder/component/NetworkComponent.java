package com.pleximus.pet_app.application.builder.component;

import com.pleximus.pet_app.application.builder.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by pleximus on 18/04/17.
 */
@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    Retrofit retrofit();

}
