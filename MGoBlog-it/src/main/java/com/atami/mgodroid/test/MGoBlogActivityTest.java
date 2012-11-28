package com.atami.mgodroid.test;

import android.test.ActivityInstrumentationTestCase2;
import com.atami.mgodroid.ui.MGoBlogActivity;
import com.jayway.android.robotium.solo.Solo;

public class MGoBlogActivityTest extends ActivityInstrumentationTestCase2<MGoBlogActivity> {

    private Solo solo;

    public MGoBlogActivityTest() {
        super(MGoBlogActivity.class);
    }

    public void setUp() throws Exception {
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

