package com.atami.mgodroid.io;


import com.atami.mgodroid.models.NodeIndex;
import com.squareup.tape.Task;
import retrofit.http.Callback;

import javax.inject.Inject;
import java.util.List;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

public class NodeIndexTask implements Task<Callback<List<NodeIndex>>> {

    public final static String TAG = "NodeIndexTask";

    private String parameter;
    private String value;
    private int page;
    private String tag;

    @Inject
    MGoBlogAPI api;

    /**
     * Downloads a node index page from MGoBlog, and saves it locally through the callback
     *
     * @param parameter parameter to query MGoBlog node indexes on, e.g. "type" for different sections of the site,
     *                  or "promote" for all front page content
     * @param value     value for specified parameter, e.g. "story" when used with the "type" parameter for all blog
     *                  posts, or "1" when used with "promote" parameter for all front page content.
     * @param page      page to retrieve
     * @param tag       identifier for this task
     */
    public NodeIndexTask(String parameter, String value, int page, String tag) {
        this.parameter = parameter;
        this.value = value;
        this.page = page;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void execute(Callback callback) {
        if (parameter.equals("promote")) {
            api.getFrontPage(page, callback);
        } else if (parameter.equals("type")) {
            api.getNodeIndexByType(value, page, callback);
        }
    }
}
