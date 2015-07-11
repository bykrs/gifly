
package it.yellowsnow.gifly.Interfaces;


import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.Objects.Permalink;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Title {


    @GET(Const.Get_Post_By_Title)
    void post(

            @Query("title") String title,
            Callback<List<Permalink>> callback
    );
}
