package it.yellowsnow.gifly.Objects;

/**
 * Created by bykrs on 22/10/14.
 * this is the array returned from the API method Add_Gif_Url
 * [Error] => ""
 * [name] => 36f717a
 * [src] => http://i.imgur.com/vOcxBow.gif
 * [dest_src] => media_gifly/tmp/36f717a
 * [x] => 285
 * [y] => 248
 * [title] => ""
 */

public class Add_Gif_Url {

    public String Error;
    public String name;
    public String src;
    public String dest_src;
    public int x;
    public int y;
    public String title;

    public Add_Gif_Url(String Error, String name, String src, String dest_src, int x, int y, String title) {
        this.Error = Error;
        this.name = name;
        this.src = src;
        this.dest_src = dest_src;
        this.x = x;
        this.y = y;
        this.title = title;
    }
}