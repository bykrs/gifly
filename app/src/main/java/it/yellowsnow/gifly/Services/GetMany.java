package it.yellowsnow.gifly.Services;

import it.yellowsnow.gifly.Settings.Const;
import retrofit.RestAdapter;

/**
 * Created by bykrs on 14/10/14.
 */
public class GetMany {

    public static it.yellowsnow.gifly.Interfaces.Get_Many getService() {
        return new RestAdapter.Builder()
                .setEndpoint(Const.API_PATH)
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(it.yellowsnow.gifly.Interfaces.Get_Many.class);
    }

}
