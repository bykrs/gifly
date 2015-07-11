
package it.yellowsnow.gifly.Interfaces;


import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.Objects.Permalink;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Post {


    @GET(Const.Get_Hashtag)
    void post(

            @Query("hashtag") String hashtag,
            Callback<List<Permalink>> callback
    );
}
