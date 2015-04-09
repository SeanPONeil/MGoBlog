package com.atami.mgodroid.modules;

import dagger.Module;

/**
 * Module that includes all of the app's modules. Used by Dagger
 * for compile time validation of injections and modules.
 */
@Module(
        includes = {
                MGoBlogAPIModule.class,
                OttoModule.class,
                TaskQueueModule.class,
                SessionModule.class
        }
)
public class MGoBlogAppModule {
}
