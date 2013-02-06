package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;

public class NodeActivity extends BaseActivity {

    private int nid;

    private boolean mIsDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.node_container);

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);
        nid = getIntent().getIntExtra("nid", 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.node_pane, NodeFragment.newInstance(nid),
                    String.valueOf(nid)).commit();
        }

        if (mIsDualPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.node_comment_pane,
                    NodeFragment.newInstance(nid), String.valueOf(nid)).commit();
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
}
