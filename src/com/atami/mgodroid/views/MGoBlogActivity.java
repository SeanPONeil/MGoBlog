package com.atami.mgodroid.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.atami.mgodroid.R;
import com.atami.mgodroid.util.SherlockPullToRefreshListFragment;

public class MGoBlogActivity extends SherlockFragmentActivity {

	// Left pane
	TabsAdapter mTabsAdapter;
	ViewPager mPager;

	// Right pane

	// Whether or not we are in dual-pane mode
	boolean mIsDualPane = false;

	String[] NODE_INDEX_TYPES = { "forum", "story", "blog", "link" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main_layout);
		setProgressBarIndeterminateVisibility(false);
		
		final ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
		mTabsAdapter = new TabsAdapter(this, mPager);

		for (int i = 0; i < NODE_INDEX_TYPES.length; i++) {
			Bundle args = new Bundle();
			args.putString("node_index_type", NODE_INDEX_TYPES[i]);
			mTabsAdapter.addTab(bar.newTab().setText(NODE_INDEX_TYPES[i]),
					NodeIndexListFragment.class, args);
		}

	}
	
	public static class TabsAdapter extends FragmentPagerAdapter
    implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }


        public int getCount() {
            return mTabs.size();
        }

        public SherlockPullToRefreshListFragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return (SherlockPullToRefreshListFragment) Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }


        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }


        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }


        public void onPageScrollStateChanged(int state) {
        }


        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
            //Log.v(TAG, "clicked");
            Object tag = tab.getTag();
            for (int i=0; i<mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

        public void onTabReselected(Tab tab, FragmentTransaction ft) {}

        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {}

        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {}
    }

}