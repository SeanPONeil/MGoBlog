package com.atami.mgodroid.core;


import com.atami.mgodroid.ui.NodeFragment;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.http.GET;
import retrofit.http.GsonConverter;
import retrofit.http.RestAdapter;
import retrofit.http.Server;

import javax.inject.Named;
import java.util.List;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeFragment.class
        }
)
public class APIModule {
    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    public interface MGoBlogAPI {
        @GET("node.json")
        List<NodeIndex> getNodeIndex(@Named("parameters[type]") String type,
                                     @Named("page") String page, @Named("parameters[sticky]") String sticky);

        @GET("node/{nid}.json")
        Node getNode(@Named("nid") String nid);
    }

    @Provides
    MGoBlogAPI provideAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(new Server(API_URL))
                .setClient(new DefaultHttpClient())
                .setConverter(new GsonConverter(new GsonBuilder().serializeNulls().create()))
                .build();
        return restAdapter.create(MGoBlogAPI.class);
    }
}
