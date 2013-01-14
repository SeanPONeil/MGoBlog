package com.atami.mgodroid.modules;


import com.atami.mgodroid.models.Node;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.ui.NodeCommentFragment;
import com.atami.mgodroid.ui.NodeFragment;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.http.*;

import javax.inject.Named;
import java.util.List;

@Module(
        entryPoints = {
                NodeIndexListFragment.WorkerFragment.class,
                NodeFragment.WorkerFragment.class,
                NodeCommentFragment.WorkerFragment.class
        }
)
public class MGoBlogAPIModule {

    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    public interface MGoBlogAPI {

        @GET("node.json")
        @QueryParam(name="parameters[sticky]", value="0")
        List<NodeIndex> getNodeIndex(@Named("parameters[type]") String type, @Named("page") String page);

        @GET("node/{nid}.json")
        Node getNode(@Named("nid") int nid);

        @GET("node/{nid}/comments.json")
        List<NodeComment> getNodeComments(@Named("nid") int nid);
    }

    @Provides
    MGoBlogAPI provideMGoBlogAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(new Server(API_URL))
                .setClient(new DefaultHttpClient())
                .setConverter(new GsonConverter(new GsonBuilder()
                        .serializeNulls()
                        .create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }
}
