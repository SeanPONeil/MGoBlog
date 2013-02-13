package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseFragment;

public class AboutFragment extends BaseFragment {

	public final static String TAG = "LoginFragment";

	public static AboutFragment newInstance() {
		AboutFragment f = new AboutFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.about, container, false);
    }

}
