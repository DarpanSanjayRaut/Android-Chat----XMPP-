package com.pleximus.pet_app.application.builder.module;

import android.content.Context;


import com.pleximus.pet_app.core.db.DatabaseManager;


import dagger.Module;
import dagger.Provides;

/**
 * Created by pleximus on 25/04/17.
 */
@Module
public class DatabaseModule {

    private Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    DatabaseManager providesDatabaseManager() {
        return DatabaseManager.getInstance(context);
    }

}
