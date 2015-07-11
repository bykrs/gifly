package it.yellowsnow.gifly.Settings;


public class Const {

    public static final String HOST = "http://gifly.com";
    public static final String CDN = HOST;
    public static final String APP = "Gifly";

    //public static final String HOST = "http://gifly.com";
    //public static final String CDN = "http://i.gifly.com";

    public static final String API_PATH = HOST+"/ajax/API";
    public static final String Get_Hashtag = "/?method=Get_Hashtag&limit=100&page=1&hashtag=";
    public static final String Get_Post_By_Title = "/?method=Get_Hashtag&limit=1&hashtag=&title=";
    public static final String Get_Many = "/?method=Get_Many";
    public static final String Add_Gif_Url = "/?method=Add_Gif_Url";
    public static final String Publish_Url = "/?method=Publish_Url";
    public static final String Record = "/?method=Record";

    public static final String MEDIA_PATH = CDN+"/media_gifly/";

    public static final String Next_Index = "_index";
    public static final String Hashtag_Null = "r";
    public static final String Hashtag_Null_index = Hashtag_Null+Next_Index;


    public static final int GREEN = 0xFF5cb85c;
    public static final int WHITE = 0xFFFFFFF;
    public static final int BLACK = 0xFF000000;
    public static final String FacebookAppId = "855552831146184";
    public static final String FacebookSecret = "4726ae0cacbfaf6d213fec6e9777293c";

    public static final String TwitterConsumerKey = "oxNjcbdEYuqoFM3cRMh8orT6G";
    public static final String TwitterConsumerSecret = "T0rJkECojRXLaXNOx3tzhpwsZ9bp75NfJuFVtHXgA3LTe6xs1c";
    public static final String TwitterCallbackUrl = API_PATH+"/auth/";

}
