package com.atami.mgodroid.io;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.atami.mgodroid.models.NodeIndex;
import com.squareup.tape.Task;
import retrofit.http.RestException;

import java.util.List;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

/**
 * Gets the specified page and type of NodeIndex from MGoBlog
 */
public class NodeIndexTask implements Task<NodeIndexTask.Callback> {

    private final static String TAG = "NodeIndexTask";
    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    public interface Callback {
        public void onSuccess(String type);

        public void onFailure(String type);
    }

    private int page;
    private String type;
    MGoBlogAPI api;

    public NodeIndexTask(MGoBlogAPI api, int page, String type) {
        this.api = api;
        this.page = page;
        this.type = type;
    }

    @Override
    public void execute(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NodeIndex> request;
                try {
                    request = api.getNodeIndex(type, page);
                } catch (RestException e) {
                    e.printStackTrace();
                    Log.i(TAG, "Get failure! " + type + " " + String.valueOf(page));
                    MAIN_THREAD.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(type);
                        }
                    });
                    return;
                }

                if (page == 0) {
                    NodeIndex.deleteAll(type);
                }
                for (NodeIndex ni : request) {
                    ni.save();
                }
                Log.i(TAG, "Get success! " + type + " " + String.valueOf(page));
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(type);
                    }
                });
            }
        }).start();
    }
}
