package it.yellowsnow.gifly.AddGif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import it.yellowsnow.gifly.ErrorHandler.ErrorGifly;
import it.yellowsnow.gifly.Objects.Add_Gif_Url;
import it.yellowsnow.gifly.Publish.PublishUrl;
import it.yellowsnow.gifly.R;
import it.yellowsnow.gifly.Record.Record;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Add_Gif extends Activity {


    private it.yellowsnow.gifly.Interfaces.Add_Gif api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gif);




    }

    public it.yellowsnow.gifly.Interfaces.Add_Gif getApi() {
        if (api == null) {
            api = it.yellowsnow.gifly.Services.Add_Gif.getService();
        }
        return api;
    }


    public void Fetch_Gif(URL url){

        final ErrorGifly error = new ErrorGifly();

        getApi().get(url, new Callback<List<Add_Gif_Url>>() {

            @Override
            public void success(List<Add_Gif_Url> Add_Gif_Url, Response response) {

                if (Add_Gif_Url.isEmpty()) {error.displayErrorMessage(Add_Gif.this, "There was an error");}
                //means it's all right
                if(Add_Gif_Url.get(0).Error.equals("false")){

                    String name = Add_Gif_Url.get(0).name;
                    String src = Add_Gif_Url.get(0).src;
                    String dest_src = Add_Gif_Url.get(0).dest_src;
                    int x = Add_Gif_Url.get(0).x;
                    int y = Add_Gif_Url.get(0).y;
                    String title = Add_Gif_Url.get(0).title;
                    Call_PublishUrl(name, src, dest_src, x, y, title);
                }
                else{
                    error.displayErrorMessage(Add_Gif.this, Add_Gif_Url.get(0).Error);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                error.displayErrorMessage(Add_Gif.this);
            }


        });

    }

    public void Call_PublishUrl(String name, String src, String dest_src, int x, int y, String title){

        Intent intent = new Intent(this, PublishUrl.class);
        intent.putExtra("name", name);
        intent.putExtra("src", src);
        intent.putExtra("dest_src", dest_src);
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    public void Record(View v){
        Intent intent = new Intent(this, Record.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }



    public void Upload_File(View v){


    }

    public void Test_Url(View v){
        //AddGifUrl
        EditText url_holder = (EditText) findViewById(R.id.Add_Gif_Hint);
        final ErrorGifly error = new ErrorGifly();

        try {
            URL url = new URL(url_holder.getText().toString());
            //Log.d("URL requested IS", url.toString());
            Fetch_Gif(url);

        }
        catch (MalformedURLException e) {
            error.displayErrorMessage(Add_Gif.this, "This Url is not valid");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return true;
    }



}
