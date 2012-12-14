package com.atami.mgodroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.MGoBlogConstants;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;
import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;

public class MGoBlogActivity extends BaseActivity implements MGoBlogConstants{

    private static final String STATE_MENUDRAWER = MGoBlogActivity.class.getName() + ".menuDrawer";


    // Left pane
    //ViewPager mPager;
    //TabsAdapter mAdapter;

    // Right pane
    //@InjectView(R.id.NodeFrame)
    //@Nullable
    //FrameLayout nodeFrame;

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane;

    private MenuDrawerManager mMenuDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        if(savedInstanceState == null){
            Fragment nodeIndex = NodeIndexListFragment.newInstance(nodeIndexTitles[1], nodeIndexTypes[1]);
            Fragment nodeIndexWorker = NodeIndexListFragment.WorkerFragment.newInstance(nodeIndexTypes[1]);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(nodeIndexWorker, NodeIndexListFragment.WorkerFragment.TAG);
            ft.add(android.R.id.content, nodeIndex);
            ft.commit();
        }

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setMenuView(R.layout.menudrawer);

        MenuFragment menu = (MenuFragment)getSupportFragmentManager().findFragmentById(R.id.menudrawer);
        menu.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment newNodeIndex = NodeIndexListFragment.newInstance(nodeIndexTitles[i], nodeIndexTypes[i]);
                Fragment oldWorker = getSupportFragmentManager()
                        .findFragmentByTag
                        (NodeIndexListFragment.WorkerFragment.TAG);
                Fragment newWorker = NodeIndexListFragment.WorkerFragment.newInstance(nodeIndexTypes[i]);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(android.R.id.content, newNodeIndex);
                ft.remove(oldWorker);
                ft.add(newWorker, NodeIndexListFragment.WorkerFragment.TAG);
                ft.commit();

                mMenuDrawer.closeMenu();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            getSupportActionBar().setSubtitle(R.string.app_subtitle);
        }
    }

    @Subscribe
    public void onNodeIndexItemClick(NodeIndex nodeIndex) {
        if (mIsDualPane) {
            NodeFragment nodeFragment = NodeFragment.newInstance(nodeIndex.getNid());
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.NodeFrame, nodeFragment).commit();
        } else {
            Intent intent = new Intent(this, NodeActivity.class);
            intent.putExtra("nid", nodeIndex.getNid());
            startActivity(intent);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mMenuDrawer.onRestoreDrawerState(inState.getParcelable(STATE_MENUDRAWER));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.onSaveDrawerState());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }

    public static class MenuFragment extends SherlockListFragment {

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            for(int i=0; i < nodeIndexCount; i++) {
                adapter.add(nodeIndexTitles[i]);
            }

            setListAdapter(adapter);
        }

    }
}