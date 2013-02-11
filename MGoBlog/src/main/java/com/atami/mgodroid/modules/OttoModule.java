/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atami.mgodroid.modules;

import android.os.Handler;
import android.os.Looper;
import com.atami.mgodroid.io.NodeIndexTaskService;
import com.atami.mgodroid.io.NodeTaskService;
import com.atami.mgodroid.ui.*;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.atami.mgodroid.ui.base.BaseListFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                MGoBlogActivity.class,
                NodeIndexListFragment.class,
                NodeFragment.class,
                NodeActivity.class,
                NodeCommentFragment.class,
                NodeIndexTaskService.class,
                NodeTaskService.class,
                LoginFragment.class
        }
)
public class OttoModule {

    @Provides
    @Singleton
    Bus provideBus() {
        return new AsyncBus();
    }

    /**
     * Otto EventBus that posts all events on the Android main thread
     */
    private class AsyncBus extends Bus {
        private final Handler mainThread = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    AsyncBus.super.post(event);
                }
            });
        }
    }
}
