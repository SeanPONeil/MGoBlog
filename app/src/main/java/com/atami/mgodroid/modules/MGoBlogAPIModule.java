package com.atami.mgodroid.modules;


import com.atami.mgodroid.io.*;
import com.atami.mgodroid.models.*;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.*;

import java.util.List;
import java.util.concurrent.Executor;

@Module(complete = false,
        includes = {SessionModule.class, OttoModule.class},
        injects = {
                LoginTask.class,
                NodeIndexTask.class,
                NodeTask.class,
                NodeCommentTask.class,
                CommentPostTask.class,
        }
)
public class MGoBlogAPIModule {

    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    @Provides
    MGoBlogAPI provideMGoBlogAPI(Session session) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(new Server(API_URL))
                .setExecutors(new APIExecutor(), new CallbackExecutor())
                .setHeaders(session)
                .setConverter(new GsonConverter(new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }

    public interface MGoBlogAPI {

        @GET("/node.json")
        @QueryParam(name = "parameters[sticky]", value = "0")
        void getNodeIndexByType(@Name("parameters[type]") String type, @Name("page") int page,
                                Callback<List<NodeIndex>> callback);

        @GET("/node.json")
        @QueryParams({
                @QueryParam(name = "parameters[sticky]", value = "0"),
                @QueryParam(name = "parameters[promote]", value = "1")
        })
        void getFrontPage(@Name("page") int page,
                          Callback<List<NodeIndex>> callback);

        @GET("/node/{nid}.json")
        void getNode(@Name("nid") int nid, Callback<Node> callback);

        @GET("/node/{nid}/comments.json")
        void getNodeComments(@Name("nid") int nid, Callback<List<NodeComment>> callback);

        @POST("/user/login")
        void loginUser(@SingleEntity LoginJsonObj payload, Callback<Session> callback);

        @POST("/comment.json")
        void postComment(@SingleEntity CommentJsonObj payload, Callback<Response> callback);
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
}
