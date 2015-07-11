package it.yellowsnow.gifly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.yellowsnow.gifly.Settings.Const;
import it.yellowsnow.gifly.Gifly;
import it.yellowsnow.gifly.Objects.Permalink;
import it.yellowsnow.gifly.R;

public class Post extends BaseAdapter {

    private Context mContext;
    private List<Permalink> mPermalinks = new ArrayList<Permalink>();
    public String Index = Const.Hashtag_Null_index;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.permalink, null);
        }

        final Permalink repo = mPermalinks.get(position);

        if(repo != null) {

            final VideoView myVideoView = (VideoView) row.findViewById(R.id.VideoView);
            final ImageButton share = (ImageButton) row.findViewById(R.id.ShareMe);

            if (!repo.caller.equals("")) {
                Index = repo.caller + "_index";
            }

            myVideoView.setVideoPath(get_path_of_video(repo.thumb));
            myVideoView.start();
            myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    myVideoView.start();
                }
            });

            List<String> Hashtags = new ArrayList<String>(Arrays.asList(repo.hashtag.split(" ")));
            Hashtags.remove(repo.caller);
            Hashtags.add(0, repo.caller);

            ArrayList<Button> buttons = new ArrayList<Button>();
            final RelativeLayout bg = (RelativeLayout) row.findViewById(R.id.Hashtags);

            Integer i = 0;
            for (final String Hashtag : Hashtags) {

                if (!Hashtag.equals("")) {

                    Button newButton = (Button) ((Activity) mContext).getLayoutInflater().inflate(R.layout.button, null);
                    newButton.setId(i + 1);
                    newButton.setText("#" + Hashtag);


                    if (Hashtag.equals(repo.caller)) {
                        newButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttongreen));
                    } else {

                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), Gifly.class);
                                intent.putExtra("hashtag", Hashtag);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                mContext.startActivity(intent);
                            }
                        });
                    }


                    buttons.add(newButton);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    int dpValue = 5; // margin in dips
                    float d = mContext.getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d);

                    lp.setMargins(0, 0, 0, margin);

                    if (i > 0) {
                        int id = buttons.get(i - 1).getId();
                        lp.addRule(RelativeLayout.BELOW, id);
                    }
                    i++;

                    bg.addView(newButton, lp);
                }
            }

            share.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                 Intent sendIntent = new Intent();
                 sendIntent.setAction(Intent.ACTION_SEND);
                 sendIntent.putExtra(Intent.EXTRA_TEXT, repo.hashtag.replaceAll(" ", " #") + " on Gifly!\r\n" + Const.HOST + "/" + repo.title + "/");
                 sendIntent.setType("text/plain");
                 mContext.startActivity(sendIntent);
                }

            });
        }
        return row;
    }



    static String get_path_of_video(String title){

        String out = "";
        for (int i = 0;i < title.length(); i++){
            if(i < 4) {
                out = out + (title.charAt(i)) + "/";
            }
        }

        Log.d("A video was required: ", Const.MEDIA_PATH+out+"b/"+title+".webm");
        return Const.MEDIA_PATH+out+"b/"+title+".webm";
    }

    public Post(Context context) {
        super();
        mContext = context;
    }

    public void setPosts(List<Permalink> Permalink) {
        mPermalinks = Permalink;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPermalinks.size();
    }

    @Override
    public Permalink getItem(int i) {
        return mPermalinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}