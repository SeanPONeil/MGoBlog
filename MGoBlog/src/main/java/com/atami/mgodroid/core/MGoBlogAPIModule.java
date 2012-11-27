package com.atami.mgodroid.core;


import com.atami.mgodroid.ui.NodeFragment;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.http.*;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeFragment.class,
                NodeIndexCache.class,
                NodeCache.class
        }
)
public class MGoBlogAPIModule {

    private static final String API_URL = "http://mgoblog.com/mobileservices/";

    public interface MGoBlogAPI {

        @GET("node.json")
        List<NodeIndex> getNodeIndex(@Named("parameters[type]") String type,
                                     @Named("page") String page, @Named("parameters[sticky]") String sticky);

        @GET("node/{nid}.json")
        Node getNode(@Named("nid") int nid);
    }

    @Provides @Singleton
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
