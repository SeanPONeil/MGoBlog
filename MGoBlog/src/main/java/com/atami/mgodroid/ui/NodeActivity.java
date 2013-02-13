package com.atami.mgodroid.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;
import android.support.v4.app.FragmentManager;

public class NodeActivity extends BaseActivity {

    public final static String TAG = "NodeActivity";

    private int nid;

    private boolean mIsDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* TODO: change to R.layout.node_container when the ModelLoader bug is fixed */
        setContentView(R.layout.node_container_one_pane);

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);
        nid = getIntent().getIntExtra("nid", 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.node_pane, NodeFragment.newInstance(nid),
                    NodeFragment.TAG).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showAddComment(int cid) {
        FragmentManager fm = getSupportFragmentManager();
        CommentDialog cf = CommentDialog.newInstance(cid);
        cf.show(fm, "comment");
    }
}