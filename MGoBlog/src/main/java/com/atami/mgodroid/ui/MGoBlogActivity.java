package com.atami.mgodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.List;

public class MGoBlogActivity extends BaseActivity {

    private static final String TAG = "MGoBlogActivity";
    private static final String STATE_ACTIVE_POSITION = "com.atami.mgodroid.activePosition";

    private MenuDrawer mMenuDrawer;

    private int mActivePosition = 0;
    private Handler mHandler;
    private Runnable mToggleUpRunnable;
    private boolean mDisplayUp = true;

    private boolean mIsDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mActivePosition = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
        }

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);


        //TODO: MenuDrawer will crash when switching from non static drawer to static. Issue is at
        //https://github.com/SimonVT/android-menudrawer/issues/62
        if (mIsDualPane) {
            mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT, true);
        } else {
            mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT, false);
        }

        mMenuDrawer.setContentView(R.layout.node_index_container);

        buildMenuDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.node_index_container,
                    NodeIndexListFragment.newInstance("promote", "1"), "MGoBlog").setTransition(FragmentTransaction
                    .TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

    @Subscribe
    public void onNodeIndexItemClick(NodeIndex nodeIndex) {
        Intent intent = new Intent(this, NodeActivity.class);
        intent.putExtra("nid", nodeIndex.getNid());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
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

    private void buildMenuDrawer() {
        List<Object> items = new ArrayList<Object>();
        items.add(new DrawerCategory("Navigation"));
        items.add(new DrawerItem("MGoBlog", R.drawable.ic_action_refresh));
        items.add(new DrawerItem("Diaries", R.drawable.ic_action_refresh));
        items.add(new DrawerItem("MGoBoard", R.drawable.ic_action_refresh));
        items.add(new DrawerItem("mgo.licio.us", R.drawable.ic_action_refresh));

        final ListView menuList = new ListView(this);
        MenuDrawerAdapter menuAdapter = new MenuDrawerAdapter(items);
        menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                DrawerItem di = (DrawerItem) menuList.getAdapter().getItem(position);
                Fragment f = null;
                if (di.mTitle.equals("MGoBlog")) {
                    f = NodeIndexListFragment.newInstance("promote", "1");
                } else if (di.mTitle.equals("Diaries")) {
                    f = NodeIndexListFragment.newInstance("type", "blog");
                } else if (di.mTitle.equals("MGoBoard")) {
                    f = NodeIndexListFragment.newInstance("type", "forum");
                } else if (di.mTitle.equals("mgo.licio.us")) {
                    f = NodeIndexListFragment.newInstance("type", "link");
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.node_index_container,
                        f, di.mTitle).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

                mActivePosition = position;
                mMenuDrawer.setActiveView(view, position);
                mMenuDrawer.closeMenu();
            }
        });

        menuList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mMenuDrawer.invalidate();
            }
        });

        mMenuDrawer.setMenuView(menuList);

        //"Up" button will blink until it is clicked
        if (!mIsDualPane) {
            mHandler = new Handler();
            mToggleUpRunnable = new Runnable() {
                @Override
                public void run() {
                    mDisplayUp = !mDisplayUp;
                    getActionBar().setDisplayHomeAsUpEnabled(mDisplayUp);
                    mHandler.postDelayed(mToggleUpRunnable, 500);
                }
            };

            mHandler.postDelayed(mToggleUpRunnable, 500);

            mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
                @Override
                public void onDrawerStateChange(int oldState, int newState) {
                    if (newState == MenuDrawer.STATE_OPEN) {
                        mHandler.removeCallbacks(mToggleUpRunnable);
                        if (!mDisplayUp) getActionBar().setDisplayHomeAsUpEnabled(true);
                        mMenuDrawer.setOnDrawerStateChangeListener(null);
                    }
                }
            });
        }
    }

    private static class DrawerItem {

        String mTitle;
        int mIconRes;

        DrawerItem(String title, int iconRes) {
            mTitle = title;
            mIconRes = iconRes;
        }
    }

    private static class DrawerCategory {

        String mTitle;

        DrawerCategory(String title) {
            mTitle = title;
        }
    }

    private class MenuDrawerAdapter extends BaseAdapter {

        private List<Object> mItems;

        MenuDrawerAdapter(List<Object> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof
                    DrawerItem ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof DrawerItem;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Object item = getItem(position);

            if (item instanceof DrawerCategory) {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_category, parent, false);
                }

                ((TextView) v).setText(((DrawerCategory) item).mTitle);

            } else {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_item, parent, false);
                }

                TextView tv = (TextView) v;
                tv.setText(((DrawerItem) item).mTitle);
                tv.setCompoundDrawablesWithIntrinsicBounds(((DrawerItem) item).mIconRes, 0, 0, 0);
            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
        }
    }
}