package com.atami.mgodroid.core;

import com.atami.mgodroid.ui.NodeFragment;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

import static com.atami.mgodroid.core.MGoBlogAPIModule.*;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeFragment.class
        }
)
public class APICacheModule {

    @Provides @Singleton
    NodeIndexCache provideNodeIndexCache(Bus bus, MGoBlogAPI api){
        return new NodeIndexCache(bus, api);
    }

    @Provides @Singleton
    NodeCache provideNodeCache(Bus bus, MGoBlogAPI api){
        return new NodeCache(bus, api);
    }
}
