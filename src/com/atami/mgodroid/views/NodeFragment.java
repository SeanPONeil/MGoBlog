package com.atami.mgodroid.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class NodeFragment extends Fragment {
	
	//ID of the current node
	int nid;
	
	public static NodeFragment newInstance(String nid) {
		NodeFragment f = new NodeFragment();

		Bundle args = new Bundle();
		args.putString("nid", nid);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nid = getArguments().getInt("nid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		WebView view = new WebView(getActivity());
		view.getSettings().setJavaScriptEnabled(true);
		return view;
	}

}
