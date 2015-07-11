package it.yellowsnow.gifly.Publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.ErrorHandler.ErrorGifly;
import it.yellowsnow.gifly.Gifly;
import it.yellowsnow.gifly.Objects.Permalink;
import it.yellowsnow.gifly.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PublishUrl extends Activity {

    public String name = "";
    public String src = "";
    public String dest_src = "";
    public int x = 0;
    public int y = 0;
    public String title = "";

    private it.yellowsnow.gifly.Interfaces.PublishUrl callApi;

    final ErrorGifly error = new ErrorGifly();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_url);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        src = intent.getStringExtra("src");
        dest_src = intent.getStringExtra("dest_src");
        x = intent.getIntExtra("x", 0);
        y = intent.getIntExtra("y", 0);
        title = intent.getStringExtra("title");

        if(name == null || src == null || dest_src == null || x == 0 || y == 0 || title == null){
            error.displayErrorMessage(PublishUrl.this, "there is an error and it cannot be fixed at the moment, try again");
            finish();
        }

        WebView webview = (WebView) findViewById(R.id.showgif) ;
        String summary = "<html><body style='margin: 0;padding: 0'><img width='100%' src="+ Const.HOST+"/"+dest_src+"></body></html>";
        webview.loadData(summary, "text/html", null);

    }

    public void Publish(View v){

        EditText hashtags_field = (EditText) findViewById(R.id.hashtags);

        String hashtags = hashtags_field.getText().toString();

        if(hashtags.trim().equals("")){
            error.displayErrorMessage(PublishUrl.this, "Please add some hashtags");
        }
        else{

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);

            callApi().get(hashtags, name, src, dest_src, x, y, title, new Callback<List<Permalink>>() {

                @Override
                public void success(List<Permalink> permalinks, Response response) {
                    if (permalinks.isEmpty()) {
                        error.displaySadMessage(PublishUrl.this);
                    }
                    //mAdapter.setPosts(posts);
                    Reload_Interface(permalinks.get(0).title);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    error.displayErrorMessage(PublishUrl.this);
                }
            });

        }

    }

    public it.yellowsnow.gifly.Interfaces.PublishUrl callApi(){
        if (callApi == null) {
            callApi = it.yellowsnow.gifly.Services.PublishUrl.getService();
        }
        return callApi;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }


/*
    private void displayErrorMessage(String Message) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(PublishUrl.this, Message, Toast.LENGTH_LONG).show();
    }
*/
/*
    private void displayErrorMessage() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(PublishUrl.this, "Failed to retrieve data.", Toast.LENGTH_LONG).show();
    }
*/
    public void Reload_Interface(String title){
        Intent intent = new Intent(this, Gifly.class);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
