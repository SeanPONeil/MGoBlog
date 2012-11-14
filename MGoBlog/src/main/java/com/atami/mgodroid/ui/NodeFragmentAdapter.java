package com.atami.mgodroid.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NodeFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String[] CONTENT = new String[]{"Body", "Comments"};

    int nid;

    public NodeFragmentAdapter(FragmentManager fm, int nid) {
        super(fm);
        this.nid = nid;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NodeFragment.newInstance(nid);
            case 1:
                return NodeFragment.newInstance(nid);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position){
        return CONTENT[position % CONTENT.length].toUpperCase();
    }
}
