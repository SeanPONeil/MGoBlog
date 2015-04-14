package com.atami.mgodroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.List;

public class MGoBlogActivity extends BaseActivity {

    private static final String TAG = "MGoBlogActivity";
    private static final String STATE_ACTIVE_POSITION = "com.atami.mgodroid.activePosition";
    private static final String STATE_ACTIVE_TITLE = "com.atami.mgodroid.activeTitle";

    private MenuDrawer mMenuDrawer;

    private int mActivePosition = 1;
    private Handler mHandler;
    private Runnable mToggleUpRunnable;
    private boolean mDisplayUp = true;

    private boolean mIsDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mActivePosition = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
            String mActiveTitle = savedInstanceState.getString(STATE_ACTIVE_TITLE);
            getSupportActionBar().setTitle(mActiveTitle);
        }

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT,
            MenuDrawer.MENU_DRAG_CONTENT);

        mMenuDrawer.setContentView(R.layout.node_index_container);

        buildMenuDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.node_index_container,
                            NodeIndexListFragment.newInstance("promote", "1"),
                            "MGoBlog")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
        outState.putString(STATE_ACTIVE_TITLE, getSupportActionBar().getTitle().toString());
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
        if (drawerState == MenuDrawer.STATE_OPEN
                || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }

    private void buildMenuDrawer() {
        List<Object> items = new ArrayList<Object>();
        items.add(new DrawerCategory("Navigation"));
        items.add(new DrawerItem("MGoBlog", R.drawable.ic_action_blog, R.drawable.ic_action_blog_selected));
        items.add(new DrawerItem("Diaries", R.drawable.ic_action_diary, R.drawable.ic_action_diary_selected));
        items.add(new DrawerItem("MGoBoard", R.drawable.ic_action_board, R.drawable.ic_action_board_selected));
        items.add(new DrawerItem("mgo.licio.us", R.drawable.ic_action_licious, R.drawable.ic_action_licious_selected));
        items.add(new DrawerCategory("Information"));
        items.add(new DrawerItem("Account", R.drawable.ic_action_login, R.drawable.ic_action_login_selected));
        items.add(new DrawerItem("About", R.drawable.ic_action_about, R.drawable.ic_action_about_selected));

        final ListView menuList = new ListView(this);
        final MenuDrawerAdapter menuAdapter = new MenuDrawerAdapter(items);
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
                } else if (di.mTitle.equals("Account")) {
					LoginFragment.newInstance().show(getSupportFragmentManager(), "dialog");
				} else if (di.mTitle.equals("About")) {
					AboutFragment.newInstance().show(getSupportFragmentManager(), "dialog");
				}

				if (!di.mTitle.equals("About") && !di.mTitle.equals("Account")) {
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.node_index_container, f, di.mTitle)
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
					getSupportActionBar().setTitle(di.mTitle);
                    mActivePosition = position;
                    mMenuDrawer.setActiveView(view, position);
				}

                menuAdapter.notifyDataSetChanged();
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
                    getSupportActionBar().setDisplayHomeAsUpEnabled(mDisplayUp);
                    mHandler.postDelayed(mToggleUpRunnable, 500);
                }
            };

            mHandler.postDelayed(mToggleUpRunnable, 500);
            mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
                @Override
                public void onDrawerStateChange(int oldState, int newState) {
                    if (newState == MenuDrawer.STATE_OPEN) {
                        mHandler.removeCallbacks(mToggleUpRunnable);
                        if (!mDisplayUp) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        mMenuDrawer.setOnDrawerStateChangeListener(null);
                    }
                }

                @Override public void onDrawerSlide(float openRatio, int offsetPixels) {

                }
            });
        }
    }

    private static class DrawerItem {

        String mTitle;
        int mIconRes;
        int mIconResSelected;

        DrawerItem(String title, int iconRes, int iconResSel) {
            mTitle = title;
            mIconRes = iconRes;
            mIconResSelected = iconResSel;
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
        private int curPosition;

        MenuDrawerAdapter(List<Object> items) {
            mItems = items;
            curPosition = 1;
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
            return getItem(position) instanceof DrawerItem ? 0 : 1;
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
            TextView tv = null;

            if (item instanceof DrawerCategory) {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_category,
                            parent, false);
                }

                ((TextView) v).setText(((DrawerCategory) item).mTitle);

            } else {
                v = getLayoutInflater().inflate(R.layout.menu_row_item,
                        parent, false);
                tv = (TextView) v.findViewById(R.id.item_text);
                tv.setText(((DrawerItem) item).mTitle);

                LinearLayout selected = (LinearLayout) v.findViewById(R.id.item_selected);

                if (position == mActivePosition) {
                    selected.setVisibility(View.VISIBLE);
                    tv.setTextColor(Color.parseColor("#F0D34E"));
                    tv.setCompoundDrawablesWithIntrinsicBounds(
                            ((DrawerItem) item).mIconResSelected, 0, 0, 0);
                } else {
                    selected.setVisibility(View.INVISIBLE);
                    tv.setTextColor(Color.parseColor("#E9E9E9"));
                    tv.setCompoundDrawablesWithIntrinsicBounds(
                            ((DrawerItem) item).mIconRes, 0, 0, 0);
                }

            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
        }


    }
}