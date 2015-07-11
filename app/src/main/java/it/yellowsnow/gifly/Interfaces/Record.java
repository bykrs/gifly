package it.yellowsnow.gifly.Interfaces;

import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.Objects.Permalink;
import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface Record {

    @Multipart
    @POST(Const.Record)
    void post(
            @Part("hashtags") String hashtags,
            @Part("video") TypedFile video,
            Callback<List<Permalink>> callback
    );
}