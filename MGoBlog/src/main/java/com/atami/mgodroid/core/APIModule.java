package com.atami.mgodroid.core;


import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.http.GET;
import retrofit.http.GsonConverter;
import retrofit.http.RestAdapter;
import retrofit.http.Server;

import javax.inject.Named;
import java.util.List;

public class APIModule extends AbstractModule {
    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    public interface MGoBlogAPI {
        @GET("node.json")
        List<NodeIndex> getNodeIndex(@Named("parameters[type]") String type, @Named("page") String page);
    }

    @Override
    protected void configure() {
    }

    @Provides
    MGoBlogAPI mGoBlogAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(new Server(API_URL))
                .setClient(new DefaultHttpClient())
                .setConverter(new GsonConverter(new Gson()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }
}
