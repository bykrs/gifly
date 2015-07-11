package it.yellowsnow.gifly.Services;

import it.yellowsnow.gifly.Settings.Const;
import retrofit.RestAdapter;

/**
 * Created by bykrs on 02/09/14.
 */
public class Post {

        public static it.yellowsnow.gifly.Interfaces.Post getService() {
            return new RestAdapter.Builder()
                    .setEndpoint(Const.API_PATH)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build()
                    .create(it.yellowsnow.gifly.Interfaces.Post.class);
        }

}
