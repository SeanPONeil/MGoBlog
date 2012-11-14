package com.atami.mgodroid.ui.base;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.atami.mgodroid.MGoBlogApplication;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class BaseActivity extends SherlockFragmentActivity{

    @Inject
    Bus bus;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Android constructs Fragment instances so we must find the ObjectGraph
        // instance and inject this.
        ((MGoBlogApplication) getApplication()).objectGraph().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }
}
