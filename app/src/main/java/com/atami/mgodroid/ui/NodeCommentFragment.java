package com.atami.mgodroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ModelLoader;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.NodeCommentTaskStatus;
import com.atami.mgodroid.io.NodeCommentTask;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.ui.base.BaseListFragment;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NodeCommentFragment extends BaseListFragment implements
        LoaderManager.LoaderCallbacks<List<NodeComment>> {

    public final static String TAG = "NodeCommentFragment";
    public final static String ASC = "ASC";
    public final static String DESC = "DESC";
    public final static String SORT = "sort";
    // ID of the current node
    int nid;
    @Inject
    TaskQueue<NodeCommentTask> queue;
    private NodeCommentAdapter mAdapter;
    private Menu nodeCommentMenu;

    public static NodeCommentFragment newInstance(int nid) {
        NodeCommentFragment f = new NodeCommentFragment();

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
            queue.add(new NodeCommentTask(nid, getTag()));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new NodeCommentAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                new ArrayList<NodeComment>(), getActivity()
                .getSupportFragmentManager(), nid);
        getListView().setDividerHeight(0);
        getListView().setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void onNewNodeCommentTaskStatus(NodeCommentTaskStatus status) {
        if (status.tag.equals(getTag())) {
            if (status.running) {
                setRefreshActionItemState(true);
            } else {
                setRefreshActionItemState(false);
            }
        }
    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (nodeCommentMenu == null) {
            return;
        }
        final MenuItem refreshItem = nodeCommentMenu.findItem(R.id.refresh);
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
        inflater.inflate(R.menu.comment, menu);
        nodeCommentMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                setRefreshActionItemState(true);
                queue.add(new NodeCommentTask(nid, getTag()));
                System.out.println(mAdapter.getCount());
                return true;
            case R.id.addComment:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CommentDialogFragment cd = CommentDialogFragment
                        .newInstance(0, nid);
                cd.show(fm, "dialog");
                return true;
            case R.id.sort_asc:
                sort(ASC);
                return true;
            case R.id.sort_desc:
                sort(DESC);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sort(String type) {

        SharedPreferences prefs = getActivity().getSharedPreferences(
                getActivity().getPackageName(), Context.MODE_PRIVATE);

        if (type.equals(ASC)) {
            prefs.edit().putString(SORT, ASC).commit();
        } else {
            prefs.edit().putString(SORT, DESC).commit();
        }
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<List<NodeComment>> onCreateLoader(int i, Bundle bundle) {
        // http://api.drupal.org/api/drupal/modules%21comment%21comment.module/function/comment_get_thread/7

        SharedPreferences prefs = getActivity().getSharedPreferences(
                getActivity().getPackageName(), Context.MODE_PRIVATE);
        String sort = prefs.getString(SORT, ASC);
        From query;

        if (sort.equals(ASC)) {
            query = new Select().from(NodeComment.class).where("nid = ?", nid)
                    .orderBy("substr(thread, 1, length(thread)-1) ASC");
        } else {
            query = new Select().from(NodeComment.class).where("nid = ?", nid)
                    .orderBy("thread DESC");
        }

        return new ModelLoader<NodeComment>(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<List<NodeComment>> listLoader,
                               List<NodeComment> nodeComments) {
        mAdapter.setNodeComments(nodeComments);
        mAdapter.notifyDataSetChanged();
        if (!nodeComments.isEmpty()) {
            setListShown(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NodeComment>> listLoader) {
    }

}
