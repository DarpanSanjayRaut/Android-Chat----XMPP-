package com.pleximus.pet_app.application.builder.module;

import com.pleximus.pet_app.core.bus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pleximus on 25/04/17.
 */
@Module
public class RxModule {


    @Provides
    RxBus provideRxBus() {
        return new RxBus();
    }
}
