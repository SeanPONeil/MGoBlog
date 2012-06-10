package com.atami.mgodroid.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atami.mgodroid.util.SherlockWebViewFragment;

public class NodeFragment extends SherlockWebViewFragment implements
		LoaderCallbacks<Cursor> {

	// ID of the current node
	int nid;
	
	//Body of the node
	String body;

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
		super.onCreateView(inflater, container, savedInstanceState);
		getWebView().getSettings().setJavaScriptEnabled(true);
		return getWebView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		
	}

	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}

}
