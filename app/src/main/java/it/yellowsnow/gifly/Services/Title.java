package it.yellowsnow.gifly.Services;

import it.yellowsnow.gifly.Settings.Const;
import retrofit.RestAdapter;

/**
 * Created by bykrs on 02/09/14.
 */
public class Title {

    public static it.yellowsnow.gifly.Interfaces.Title getService() {
        return new RestAdapter.Builder()
                .setEndpoint(Const.API_PATH)
                        //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(it.yellowsnow.gifly.Interfaces.Title.class);
    }

}
