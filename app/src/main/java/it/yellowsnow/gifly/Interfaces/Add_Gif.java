package it.yellowsnow.gifly.Interfaces;

import java.net.URL;
import java.util.List;

import it.yellowsnow.gifly.Objects.Add_Gif_Url;
import it.yellowsnow.gifly.Settings.Const;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by bykrs on 14/10/14.
 */
public interface Add_Gif {


    @GET(Const.Add_Gif_Url)
    void get(
            @Query("url") URL title,
            Callback<List<Add_Gif_Url>> callback
    );
}