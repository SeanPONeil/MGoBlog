package com.atami.mgodroid.ui.base;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import com.atami.mgodroid.MGoBlogApplication;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * All fragments should extend this for dependency injection.
 */
public class BaseFragment extends SherlockFragment {

    @Inject
    protected Bus bus;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Android constructs Fragment instances so we must find the ObjectGraph
        // instance and inject this.
        ((MGoBlogApplication) getActivity().getApplication()).objectGraph().inject(this);
    }

    /*
       savedInstanceState will always return null in a Fragment unless
       there's a Bundle explicitly passed back. This is different from how
       the Activity savedInstanceState API works, and we need this to get the
       first run of onCreate in a Fragment.
       */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("", "");
        super.onSaveInstanceState(outState);
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