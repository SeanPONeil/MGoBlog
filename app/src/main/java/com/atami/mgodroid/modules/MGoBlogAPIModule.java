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
    MGoBlogAPI provideMGoBlogAPI(MGoRequestInterceptor MGoRequestInterceptor) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setExecutors(new APIExecutor(), new CallbackExecutor())
                .setRequestInterceptor(MGoRequestInterceptor)
                .setConverter(new GsonConverter(
                    new GsonBuilder().setPrettyPrinting().serializeNulls().create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }

    public interface MGoBlogAPI {

        @GET("/node.json?parameters[sticky]=0")
        void getNodeIndexByType(@Path("parameters[type]") String type, @Path("page") int page,
                                Callback<List<NodeIndex>> callback);

        @GET("/node.json?parameters[sticky]=0&parameters[promote]=1")
        void getFrontPage(@Path("page") int page,
                          Callback<List<NodeIndex>> callback);

        @GET("/node/{nid}.json")
        void getNode(@Path("nid") int nid, Callback<Node> callback);

        @GET("/node/{nid}/comments.json")
        void getNodeComments(@Path("nid") int nid, Callback<List<NodeComment>> callback);

        @POST("/user/login")
        void loginUser(@Body LoginJsonObj payload, Callback<MGoRequestInterceptor> callback);

        @POST("/comment.json")
        void postComment(@Body CommentJsonObj payload, Callback<Response> callback);
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
