package com.atami.mgodroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ModelLoader;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.NodeTaskStatus;
import com.atami.mgodroid.io.NodeTask;
import com.atami.mgodroid.models.Node;
import com.atami.mgodroid.ui.base.WebViewFragment;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;
import java.util.List;

public class NodeFragment extends WebViewFragment implements
        LoaderManager.LoaderCallbacks<List<Node>> {

    public final static String TAG = "NodeFragment";

    // ID of the current node
    int nid;

    Node node;

    @Inject
    TaskQueue<NodeTask> queue;

    private Menu nodeMenu;

    public static NodeFragment newInstance(int nid) {
        NodeFragment f = new NodeFragment();

        Bundle args = new Bundle();
        args.putInt("nid", nid);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        nid = getArguments().getInt("nid");

        if (savedInstanceState == null) {
            queue.add(new NodeTask(nid, getTag()));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setDefaultFontSize(16);
        getWebView().getSettings().setPluginState(WebSettings.PluginState.ON);
        getWebView().getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        // getWebView().setBackgroundDrawable(getResources().getDrawable(R.drawable.comment_card));
        // getWebView().setBackgroundColor(0x00000000);
        // getWebView().setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        // Bug in ActiveAndroid ModelLoader: Cached results in LoaderManager
        // aren't updated
        // when the ModelLoaders From changes
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void onNewNodeTaskStatus(NodeTaskStatus status) {
        // System.out.println(status.tag);
        // System.out.println(getTag());
        if (getTag() != null) {
            if (status.tag.equals(getTag())) {
                if (status.running) {
                    setRefreshActionItemState(true);
                } else {
                    setRefreshActionItemState(false);
                }
            }
        }
    }

    @Override
    public Loader<List<Node>> onCreateLoader(int id, Bundle args) {
        From query = new Select().from(Node.class).where("nid = ?", nid);
        return new ModelLoader<Node>(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<List<Node>> nodeLoader, List<Node> node) {
        if (!node.isEmpty()) {
            System.out.println(node.get(0).toString());
            getWebView().loadDataWithBaseURL("file:///android_asset/",
                    node.get(0).getBody(), "text/html", "UTF-8", null);
            getSherlockActivity().getSupportActionBar().setTitle(
                    node.get(0).getTitle());

            String name = node.get(0).getName();
            if (name.isEmpty()) {
                name = "Anonymous Coward";
            }
            getSherlockActivity().getSupportActionBar().setSubtitle(
                    "By " + name + " - " + node.get(0).getCommentCount()
                            + " " + "comments");
            this.node = node.get(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Node>> nodeLoader) {
    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (nodeMenu == null) {
            return;
        }
        final MenuItem refreshItem = nodeMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View refreshView = inflater.inflate(R.layout.refresh_menu_item,
                        null);
                refreshItem.setActionView(refreshView);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.node_body, menu);
        nodeMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                queue.add(new NodeTask(nid, getTag()));
                return true;
            case R.id.comments:
                // Instantiate a new fragment.
                NodeCommentFragment newFragment = NodeCommentFragment
                        .newInstance(nid);

                // Add the fragment to the activity, pushing this transaction
                // on to the back stack.
                FragmentTransaction ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                        R.anim.fragment_slide_left_exit,
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_right_exit);
                ft.replace(R.id.node_pane, newFragment, getTag());
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.share:
                if(node != null){
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(android.content.Intent.EXTRA_SUBJECT, node.getTitle());
                    i.putExtra(android.content.Intent.EXTRA_TEXT, "http://mgoblog.com/" + node.getPath());
                    startActivity(Intent.createChooser(i, "Share this Post"));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
