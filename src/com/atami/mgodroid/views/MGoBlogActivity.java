package com.atami.mgodroid.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.atami.mgodroid.R;

public class MGoBlogActivity extends SherlockFragmentActivity {

	//Left pane
	NodeIndexAdapter mAdapter;
	ViewPager mPager;
	
	//Right pane
	
	// Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main_layout);
		setProgressBarIndeterminateVisibility(false);
		
		mAdapter = new NodeIndexAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
		mPager.setAdapter(mAdapter);
	}

	public static class NodeIndexAdapter extends FragmentPagerAdapter {

		int NUM_ITEMS = 4;

		String[] NODE_INDEX_TYPES = { "forum", "story", "blog", "link" };

		public NodeIndexAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return NodeIndexListFragment
					.newInstance(NODE_INDEX_TYPES[position]);
		}
	}

}
