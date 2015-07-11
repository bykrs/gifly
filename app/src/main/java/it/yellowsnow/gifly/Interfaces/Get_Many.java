package it.yellowsnow.gifly.Interfaces;

import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by bykrs on 14/10/14.
 */
public interface Get_Many {


    @GET(Const.Get_Many)
    void get(
            Callback<List<it.yellowsnow.gifly.Objects.Get_Many>> callback
    );
}