package com.pleximus.pet_app.application.builder.module;

import com.pleximus.pet_app.core.api.APIInteface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by pleximus on 21/04/17.
 */
@Module
public class ApiModule {

    @Provides
    APIInteface providesRetrofitApiService(Retrofit retrofit) {
        return retrofit.create(APIInteface.class);
    }

}
