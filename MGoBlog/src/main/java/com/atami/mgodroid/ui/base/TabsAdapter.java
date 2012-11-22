package com.atami.mgodroid.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;

/**
 * Base class for implementing "swipeable" Action Bar tabs.
 */
public class TabsAdapter extends FragmentPagerAdapter
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

    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }


    public void onPageScrollStateChanged(int state) {
    }


    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }
}