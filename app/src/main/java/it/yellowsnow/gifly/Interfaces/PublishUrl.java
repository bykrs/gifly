package it.yellowsnow.gifly.Interfaces;

import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.Objects.Permalink;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by bykrs on 14/10/14.
 */
public interface PublishUrl {

    @GET(Const.Publish_Url)
    void get(
            @Query("hashtags") String hashtags,
            @Query("name") String name,
            @Query("src") String src,
            @Query("dest_src") String dest_src,
            @Query("x") int x,
            @Query("y") int y,
            @Query("title") String title,
            Callback<List<Permalink>> callback
    );
}
