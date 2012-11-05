package com.atami.mgodroid.ui;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.atami.mgodroid.R;

public class NodeIndexListFragmentAdapter extends FragmentPagerAdapter {

    private Resources res;

    public NodeIndexListFragmentAdapter(Resources res, FragmentManager fm) {
        super(fm);
        this.res = res;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NodeIndexListFragment.newInstance(res.getString(R.string.mgoboard_type));
            case 1:
                return NodeIndexListFragment.newInstance(res.getString(R.string.mgoblog_type));
            case 2:
                return NodeIndexListFragment.newInstance(res.getString(R.string.diaries_type));
            case 3:
                return NodeIndexListFragment.newInstance(res.getString(R.string.mgolicious_type));
            default:
                //will never get here
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return res.getString(R.string.mgoboard_title);
            case 1:
                return res.getString(R.string.mgoblog_title);
            case 2:
                return res.getString(R.string.diaries_title);
            case 3:
                return res.getString(R.string.mgolicious_title);
            default:
                //will never get here
                return null;
        }
    }
}