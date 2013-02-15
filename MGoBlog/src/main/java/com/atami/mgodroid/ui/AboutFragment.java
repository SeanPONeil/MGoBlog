package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		View v = inflater.inflate(R.layout.about, container, false);

		TextView tv = (TextView) v.findViewById(R.id.about);
		tv.setText(Html
				.fromHtml("MGoBlog covers football, basketball, hockey, baseball, lacrosse, recruiting, and much, much more. This app was designed by two University of Michigan Alumni, Sean O'Neil and Kristin Boyer. " +
						"It is open source and can be checked out on <a href=\"https://github.com/SeanPONeil/MGoBlog\">GitHub</a>."));

		return v;
	}

}
