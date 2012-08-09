package com.atami.mgodroid.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.atami.mgodroid.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MGoBlogActivity extends SherlockFragmentActivity implements
		NodeIndexListFragment.OnNodeIndexItemClickListener {

	// Left pane
	ViewPager mPager;
	PageIndicator mIndicator;
	NodeIndexListFragmentAdapter mAdapter;

	// Right pane
	FrameLayout nodeFrame;

	// Whether or not we are in dual-pane mode
	boolean mIsDualPane = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		nodeFrame = (FrameLayout) findViewById(R.id.NodeFrame);
		
		mAdapter = new NodeIndexListFragmentAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);

		mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);
	}

	@Override
	public void onNodeIndexItemClick(int nid) {
		if (mIsDualPane) {
			NodeFragment nodeFragment = NodeFragment.newInstance(nid);
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.NodeFrame, nodeFragment).commit();
		} else {
			Log.d("DEBUG", "reached non dual pane");
			Intent intent = new Intent(this, NodeActivity.class);
			intent.putExtra("nid", nid);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.mgoblog, menu);
		return true;
	}
	
	private class NodeIndexListFragmentAdapter extends FragmentPagerAdapter{
		
		protected final String[] NODE_INDEX_TITLES = { "MGoBoard", "MGoBlog", "Diaries", "mgo.licio.us" };

	    public NodeIndexListFragmentAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int position) {
	        return NodeIndexListFragment.newInstance(position);
	    }

	    @Override
	    public int getCount() {
	        return NODE_INDEX_TITLES.length;
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return NODE_INDEX_TITLES[position];
	    }
	}
}