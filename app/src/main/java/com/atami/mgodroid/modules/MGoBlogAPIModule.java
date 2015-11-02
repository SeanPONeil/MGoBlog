package com.atami.mgodroid.modules;


import android.content.Context;

import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.io.*;
import com.atami.mgodroid.models.*;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import dagger.Module;
import dagger.Provides;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Module(complete = false,
        library = true,
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
    private static long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MB

    Context context;

    public MGoBlogAPIModule(Context context) {
        this.context = context;
    }

    @Provides
    Interceptor provideInterceptor() {
        return new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                // Add Cache Control only for GET methods
                if (request.method().equals("GET")) {
                    if (MGoBlogApplication.isOnline()) {
                        // 1 day
                        request.newBuilder()
                                .header("Cache-Control", "only-if-cached")
                                .build();
                    } else {
                        // 4 weeks stale
                        request.newBuilder()
                                .header("Cache-Control", "public, max-stale=2419200")
                                .build();
                    }
                }

                com.squareup.okhttp.Response response = chain.proceed(request);

                // Re-write response CC header to force use of cache
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=86400") // 1 day
                        .build();
            }
        };
    }

    @Provides
    OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        // Create Cache
        Cache cache = null;
        cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);

        // Create OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

        // Add Cache-Control Interceptor
        okHttpClient.networkInterceptors().add(interceptor);

        return okHttpClient;
    }

    @Provides
    MGoBlogAPI provideMGoBlogAPI(MGoRequestInterceptor MGoRequestInterceptor, OkHttpClient client) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                //.setClient(new OkClient(client))
                .setEndpoint(API_URL)
                .setExecutors(new APIExecutor(), new CallbackExecutor())
                .setRequestInterceptor(MGoRequestInterceptor)
                .setConverter(new GsonConverter(
                        new GsonBuilder()
                                .excludeFieldsWithoutExposeAnnotation()
                                .setPrettyPrinting()
                                .serializeNulls()
                            .create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }

    public interface MGoBlogAPI {

        @GET("/node.json?parameters[sticky]=0")
        void getNodeIndexByType(@Query("parameters[type]") String type, @Query("page") int page,
                                Callback<List<NodeIndex>> callback);

        @GET("/node.json?parameters[sticky]=0&parameters[promote]=1")
        void getFrontPage(@Query("page") int page,
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
