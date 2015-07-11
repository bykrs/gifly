package it.yellowsnow.gifly;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import it.yellowsnow.gifly.AddGif.Add_Gif;
import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.ErrorHandler.ErrorGifly;
import it.yellowsnow.gifly.Interfaces.Get_Many;
import it.yellowsnow.gifly.Interfaces.Title;
import it.yellowsnow.gifly.Objects.Permalink;
import it.yellowsnow.gifly.Services.GetMany;
import it.yellowsnow.gifly.Services.Post;
import it.yellowsnow.gifly.Helpers.TinyDB;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Gifly extends ListActivity {

    public String hashtag = "";
    public String title = "";
    public boolean next = true;
    public int index = 0;

    //Hashtagged intent
    private it.yellowsnow.gifly.Adapters.Post mAdapter;
    private it.yellowsnow.gifly.Interfaces.Post api;

    //Get by title interface
    private Title title_api;

    //Without hashtag
    private Get_Many Get_Many_api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ErrorGifly error = new ErrorGifly();


        //Log.d("Process", "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifly);

        final TinyDB tinydb = new TinyDB(this);
        final Button button_next = (Button) findViewById(R.id.Footer_Start_Over);


        button_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Gifly.class);
            intent.putExtra("next", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            }
        });


        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        next = intent.getBooleanExtra("next", true);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            hashtag = intent.getStringExtra(SearchManager.QUERY);
            index = MoveTo(hashtag + Const.Next_Index, next);
            //Log.d("Process", "found a querystring: "+hashtag);
        }
        else{
            hashtag = intent.getStringExtra("hashtag");

            if(hashtag != null) {
                index = MoveTo(hashtag + Const.Next_Index, next);
                //Log.d("Process", "found an hashtag from click: " + hashtag);
            }
        }

        if(hashtag != null) {
            button_next.setText(R.string.Start_Over);
            button_next.setBackgroundColor(Const.BLACK);
        }
        else{
            button_next.setBackgroundColor(Const.GREEN);

        }

        //if there's a specific call to a title
        if(title != null){
            //Log.d("Process", "Found a title: "+title);
            mAdapter = new it.yellowsnow.gifly.Adapters.Post(this);
            setListAdapter(mAdapter);
            title_api().post(title, new Callback<List<Permalink>>() {

                @Override
                public void success(List<Permalink> permalinks, Response response) {
                    if (permalinks.isEmpty()) {
                        error.displaySadMessage(Gifly.this);
                    }

                    mAdapter.setPosts(permalinks);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    error.displayErrorMessage(Gifly.this);
                }
            });
        }
        else{

            //we call the arraylist r if there's no hashtag
            if(hashtag == null){

                //Log.d("Process", "No hashtag required, coming with index "+ index);
                //index = intent.getIntExtra(Const.Hashtag_Null_index,0);

                index = MoveTo(Const.Hashtag_Null_index, next);
                //check if we have an already set list of titles to be shown
                final ArrayList<String> List_Of_Titles = tinydb.getList(Const.Hashtag_Null);
                //if the list is not empty that means that we have a number of titles to be displayed
                if(!List_Of_Titles.isEmpty()){
                    //Log.d("Process", "Found a list of titles to be shown");
                    Reload_Interface(Const.Hashtag_Null, List_Of_Titles, index);
                    finish();
                }
                else {
                    //Log.d("Process", "Ask the list of titles from the API");
                    //use the api in order to fetch a list of titles
                    Get_ManyApi().get(new Callback<List<it.yellowsnow.gifly.Objects.Get_Many>>() {

                        @Override
                        public void success(List<it.yellowsnow.gifly.Objects.Get_Many> Get_Many, Response response) {

                            if (Get_Many.isEmpty()) {error.displaySadMessage(Gifly.this);}

                            //save the list of titles and reload the interface with new list of titles
                            tinydb.putList(Const.Hashtag_Null, Get_Many.get(0).titles);
                            tinydb.putInt(Const.Hashtag_Null_index, 0);
                            Reload_Interface(Const.Hashtag_Null, Get_Many.get(0).titles, 0);
                            finish();

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            error.displayErrorMessage(Gifly.this);
                        }
                    });
                }
            }
            else{
                //Log.d("Process", "Hashtag found: "+hashtag);
                mAdapter = new it.yellowsnow.gifly.Adapters.Post(this);
                setListAdapter(mAdapter);
                getApi().post(hashtag, new Callback<List<Permalink>>() {

                    @Override
                    public void success(List<Permalink> permalinks, Response response) {
                        if (permalinks.isEmpty()) {
                            error.displaySadMessage(Gifly.this);
                        }

                        mAdapter.setPosts(permalinks);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        error.displayErrorMessage(Gifly.this);
                    }
                });
            }
        }
    }

    public void Reload_Interface(String Hashtag, ArrayList Titles, int index){
        Intent intent = new Intent(this, Gifly.class);
        title = GetNextRecord(Hashtag, Titles, index);
        intent.putExtra("title", title);
        //Log.d("Process", "setting new intent title="+title+" index: "+index);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public String GetNextRecord(String Hashtag, ArrayList Titles, int index){

        String title = "";
        if(!Hashtag.isEmpty()){
            title = Titles.get(index).toString();
        }

        if(title == null){
            return "";
        }

        //Log.d("Process", "Next record_publish is "+title+" Index is: "+index);
        return title;
    }

    public it.yellowsnow.gifly.Interfaces.Post getApi() {
        if (api == null) {
            api = Post.getService();
        }
        return api;
    }

    public Title title_api() {
        if (title_api == null) {
            title_api = it.yellowsnow.gifly.Services.Title.getService();
        }
        return title_api;
    }

    public Get_Many Get_ManyApi(){
        if (Get_Many_api == null) {
            Get_Many_api = GetMany.getService();
        }
        return Get_Many_api;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        //String [] popular_hashtags = getResources().getStringArray(R.array.popular_hashtags);

        if(hashtag == null){
            this.setTitle("");
        }
        else {
            this.setTitle("#" + hashtag);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setQuery(hashtag, false);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Add_Gif:
                Open_Add_Gif();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    public int MoveTo(String key, Boolean next){

        TinyDB tinydb = new TinyDB(this);
        index = tinydb.getInt(key);

        //Log.d("MoveTo", "Index was: "+index);

        if(next){
            index++;
        }
        else{
            index--;
        }

        //Log.d("MoveTo", "Index now is: "+index);

        tinydb.putInt(key, index);
        return index;
    }

    public void Open_Add_Gif(){
        Intent intent = new Intent(this, Add_Gif.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}