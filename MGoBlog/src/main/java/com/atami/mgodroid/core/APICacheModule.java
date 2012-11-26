package com.atami.mgodroid.core;

import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                NodeIndexListFragment.class
        }
)
public class APICacheModule {

    @Provides @Singleton
    NodeIndexCache provideAPICache(Bus bus, MGoBlogAPIModule.MGoBlogAPI api){
        return new NodeIndexCache(bus, api);
    }
}
