package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.support.v4.app.DialogFragment;
import com.atami.mgodroid.R;

public class CommentDialogFragment extends DialogFragment {
	
	int cid;

	public static CommentDialogFragment newInstance(int cid) {
		CommentDialogFragment f = new CommentDialogFragment();

		Bundle args = new Bundle();
		args.putInt("cid", cid);
		f.setArguments(args);

		return f;
	}
	
	public CommentDialogFragment(){
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cid = getArguments().getInt("cid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		View v = inflater.inflate(R.layout.comment, container, false);
		
		if(cid == -1){
			//new one
		} else {
			//reply
		}
		
		return v;
	}
}