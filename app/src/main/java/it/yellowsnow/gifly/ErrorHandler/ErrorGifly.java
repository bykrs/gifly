package it.yellowsnow.gifly.ErrorHandler;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by bykrs on 27/10/14.
 */
public class ErrorGifly {

    public static void displaySadMessage(Context context) {
        Toast.makeText(context, "I'm sorry, there was an error", Toast.LENGTH_LONG).show();
    }

    public static void displayErrorMessage(Context context, String xxx) {
        Toast.makeText(context, xxx, Toast.LENGTH_LONG).show();
    }

    public static void displayErrorMessage(Context context) {
        Toast.makeText(context, "Failed to retrieve data.", Toast.LENGTH_LONG).show();
    }

}
