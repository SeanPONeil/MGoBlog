package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.APIModule.MGoBlogAPI;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.squareup.otto.Bus;
import com.viewpagerindicator.TitlePageIndicator;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MGoBlogActivity extends RoboSherlockFragmentActivity {

    // Left pane
    @InjectView(R.id.NodeIndexViewPager)
    ViewPager mPager;

    @InjectView(R.id.indicator)
    TitlePageIndicator mIndicator;

    // Right pane
    //@InjectView(R.id.NodeFrame)
    //FrameLayout nodeFrame;

    //Action Bar
    @InjectResource(R.string.app_name)
    String appName;
    @InjectResource(R.string.app_subtitle)
    String appSubtitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle(appSubtitle);

        mPager.setAdapter(new NodeIndexListFragmentAdapter(getResources(), getSupportFragmentManager()));
        mIndicator.setViewPager(mPager);
    }

//	@Subscribe
//	public void onNodeIndexItemClick(NodeIndexItemClick c){
//		if (mIsDualPane) {
//			NodeFragment nodeFragment = NodeFragment.newInstance(c.nid);
//			FragmentTransaction ft = getSupportFragmentManager()
//					.beginTransaction();
//			ft.replace(R.id.NodeFrame, nodeFragment).commit();
//		} else {
//			Log.d("DEBUG", "reached non dual pane");
//			Intent intent = new Intent(this, NodeActivity.class);
//			intent.putExtra("nid", c.nid);
//			startActivity(intent);
//		}
//	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.mgoblog, menu);
        return true;
    }
}