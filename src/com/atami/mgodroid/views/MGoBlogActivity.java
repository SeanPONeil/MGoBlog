package com.atami.mgodroid.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.atami.mgodroid.R;

public class MGoBlogActivity extends FragmentActivity {

	NodeIndexAdapter mAdapter;
	ViewPager mPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
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
