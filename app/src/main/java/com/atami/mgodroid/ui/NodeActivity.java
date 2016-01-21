package com.atami.mgodroid.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.hyprmx.android.sdk.HyprMXPresentation;
import com.hyprmx.android.sdk.api.data.Offer;
import com.hyprmx.android.sdk.api.data.OffersAvailableResponse;
import com.hyprmx.android.sdk.utility.OnOffersAvailableResponseListener;

public class NodeActivity extends BaseActivity {

    public final static String TAG = "NodeActivity";

    private int nid;

    private boolean mIsDualPane;

    private HyprMXPresentation presentation;

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

        presentation = new HyprMXPresentation();
        presentation.prepare(new OnOffersAvailableResponseListener() {
            @Override
            public void onError(int i, Exception e) {
                Toast.makeText(NodeActivity.this,
                        "Prepare failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onOffersAvailable(OffersAvailableResponse offersAvailable) {
                for (Offer offer : offersAvailable.getOffersAvailable()) {

                    /**
                     * We generate a unique transaction ID for each offer, and automatically send it back to you in the callback.
                     * If you would like to provide your own transaction ID you can do so by setting the transaction ID on the offer
                     * you get here. You can verify the value set here is matched against the offer passed to the onOfferCompleted callback.
                     *
                     */
                    offer.setTransactionId("PartnerCode");
                }
                Toast.makeText(NodeActivity.this,
                        "Offer Available", Toast.LENGTH_SHORT).show();
                presentation.show(NodeActivity.this);
            }

            @Override
            public void onNoOffersAvailable(OffersAvailableResponse offersAvailableResponse) {
                Toast.makeText(NodeActivity.this,
                        "No Offers", Toast.LENGTH_SHORT).show();`
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}