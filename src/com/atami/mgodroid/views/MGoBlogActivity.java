package com.atami.mgodroid.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.atami.mgodroid.R;

public class MGoBlogActivity extends FragmentActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        NodeIndexListFragment list = NodeIndexListFragment.newInstance("story");

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.test_frame, list);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        
	}

}
