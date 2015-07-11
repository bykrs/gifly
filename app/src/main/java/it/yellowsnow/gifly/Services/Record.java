package it.yellowsnow.gifly.Services;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import it.yellowsnow.gifly.Settings.Const;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class Record {

    public static it.yellowsnow.gifly.Interfaces.Record getService() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(0, TimeUnit.SECONDS);
        client.setReadTimeout(0, TimeUnit.SECONDS);

        return new RestAdapter.Builder()
                .setEndpoint(Const.API_PATH)
                .setClient(new OkClient(client))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build()
                .create(it.yellowsnow.gifly.Interfaces.Record.class);
    }

}