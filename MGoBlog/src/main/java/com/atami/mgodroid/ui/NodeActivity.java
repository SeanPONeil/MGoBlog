package com.atami.mgodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;

public class NodeActivity extends BaseActivity {

    int nid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If we are in two pane mode, finish this activity
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.node_pane);

        if(savedInstanceState == null){
            nid = getIntent().getIntExtra("nid", 0);
            Fragment nf = NodeFragment.newInstance(nid);
            Fragment worker = NodeFragment.WorkerFragment.newInstance(nid);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_pane, nf).add(worker,
                    NodeFragment.WorkerFragment.TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MGoBlogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
