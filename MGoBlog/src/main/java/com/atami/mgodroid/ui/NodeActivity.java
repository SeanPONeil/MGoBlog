package com.atami.mgodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.node_pane);

        if (savedInstanceState == null) {
            nid = getIntent().getIntExtra("nid", 0);
            NodeFragment nf = NodeFragment.newInstance(nid);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_pane, nf,
                    String.valueOf(nid)).commit();
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
