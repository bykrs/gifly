package it.yellowsnow.gifly.Objects;

/**
 * Created by bykrs on 02/09/14.
 */
public class Permalink {

    public int pid;
    public String title;
    public int date;
    public String hashtag;
    public String caller;
    public String thumb;

    public Permalink(int pid, String title, int date, String hashtag, String caller, String thumb) {
        this.pid = pid;
        this.title = title;
        this.date = date;
        this.hashtag = hashtag;
        this.caller = caller;
        this.thumb = thumb;
    }

}
