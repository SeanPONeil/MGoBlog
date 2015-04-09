package com.atami.mgodroid.modules;

import com.atami.mgodroid.io.LoginTaskService;
import com.atami.mgodroid.models.Session;
import com.atami.mgodroid.ui.CommentDialogFragment;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(complete = false,
        injects = {
                CommentDialogFragment.class,
                LoginTaskService.class
        })
public class SessionModule {
    @Provides
    @Singleton
    Session provideSession() {
        return new Session();
    }
}
