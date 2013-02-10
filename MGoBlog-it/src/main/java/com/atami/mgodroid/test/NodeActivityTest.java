package com.atami.mgodroid.test;


import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import com.atami.mgodroid.ui.MGoBlogActivity;
import com.atami.mgodroid.ui.NodeActivity;
import com.jayway.android.robotium.solo.Solo;

public class NodeActivityTest extends ActivityInstrumentationTestCase2<NodeActivity> {

    private Solo solo;

    public NodeActivityTest() {
        super(NodeActivity.class);
    }

    public void setUp() throws Exception {
        Intent i = new Intent();
        i.putExtra("nid", 76056);
        setActivityIntent(i);
        solo = new Solo(getInstrumentation(), getActivity());
        solo.sleep(500);
    }

    public void testActivityExists() {
        assertNotNull(solo);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
