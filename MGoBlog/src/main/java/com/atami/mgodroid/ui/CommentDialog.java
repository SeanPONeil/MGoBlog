package com.atami.mgodroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import com.atami.mgodroid.R;

public class CommentDialog extends DialogFragment {
	int cid;
	Activity a;

	static CommentDialog newInstance(int cid) {
		CommentDialog f = new CommentDialog();

		Bundle args = new Bundle();
		args.putInt("cid", cid);
		f.setArguments(args);

		return f;
	}
	
	public CommentDialog(){
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cid = getArguments().getInt("cid");
		a = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comment, container, false);
		
		if(cid == -1){
			//new one
		} else {
			//reply
		}
		
		return v;
	}
}