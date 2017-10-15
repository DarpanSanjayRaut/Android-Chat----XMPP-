package com.pleximus.pet_app.smack.ui.dagger.component;

import com.pleximus.pet_app.application.builder.component.ApiComponent;
import com.pleximus.pet_app.smack.ui.ChatMessageActivity;
import com.pleximus.pet_app.smack.ui.dagger.module.ChatMessageModule;
import com.pleximus.pet_app.smack.ui.dagger.scope.ChatMessageScope;

import dagger.Component;

/**
 * Created by pleximus on 02/05/17.
 */
@ChatMessageScope
@Component(modules = ChatMessageModule.class, dependencies = ApiComponent.class)
public interface ChatMessageComponent {
    void inject(ChatMessageActivity chatMessageActivity);
}
