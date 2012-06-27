package com.atami.mgodroid.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.atami.mgodroid.R;

public class NodeActivity extends SherlockFragmentActivity {
	
	int nid;
	
	int CONTENT_VIEW_ID = 10101010;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        nid = getIntent().getIntExtra("nid", 0);
        
        //IF we are in two pane mode, finish this activity
        if(getResources().getBoolean(R.bool.has_two_panes)){
        	finish();
        	return;
        }
        
        if(savedInstanceState == null){
        	NodeFragment nodeFragment = NodeFragment.newInstance(nid);
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(android.R.id.content, nodeFragment).commit();
        }
	}
	
	

}
