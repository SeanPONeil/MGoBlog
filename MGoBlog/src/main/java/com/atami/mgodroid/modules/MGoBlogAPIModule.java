package com.atami.mgodroid.modules;


import com.atami.mgodroid.io.NodeCommentTask;
import com.atami.mgodroid.io.NodeIndexTask;
import com.atami.mgodroid.io.NodeTask;
import com.atami.mgodroid.models.Node;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.models.NodeIndex;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.http.*;

import javax.inject.Named;
import java.util.List;
import java.util.concurrent.Executor;

@Module(
        entryPoints = {
                NodeIndexTask.class,
                NodeTask.class,
                NodeCommentTask.class
        }
)
public class MGoBlogAPIModule {

    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    public interface MGoBlogAPI {

        @GET("node.json")
        @QueryParam(name = "parameters[sticky]", value = "0")
        void getNodeIndexByType(@Named("parameters[type]") String type, @Named("page") int page,
                                Callback<List<NodeIndex>> callback);

        @GET("node.json")
        @QueryParams({
                @QueryParam(name = "parameters[sticky]", value = "0"),
                @QueryParam(name = "parameters[promote]", value = "1")
        })
        void getFrontPage(@Named("page") int page,
                          Callback<List<NodeIndex>> callback);

        @GET("node/{nid}.json")
        void getNode(@Named("nid") int nid, Callback<Node> callback);

        @GET("node/{nid}/comments.json")
        void getNodeComments(@Named("nid") int nid, Callback<List<NodeComment>> callback);
    }

    private class APIExecutor implements Executor {

        @Override
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }

    private class CallbackExecutor implements Executor {

        @Override
        public void execute(Runnable r) {
            r.run();
        }
    }

    @Provides
    MGoBlogAPI provideMGoBlogAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(new Server(API_URL))
                .setClient(new DefaultHttpClient())
                .setExecutors(new APIExecutor(), new CallbackExecutor())
                .setConverter(new GsonConverter(new GsonBuilder()
                        .serializeNulls()
                        .create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }
}
