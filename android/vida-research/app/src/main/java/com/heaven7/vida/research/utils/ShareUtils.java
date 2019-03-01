package com.heaven7.vida.research.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

public class ShareUtils {

    public static void shareText(Activity activity, String text, String title){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(textIntent, title));
    }

    public static void shareImage(Activity activity, Uri imageUri, String title){
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/*"); //image/jpeg
        imageIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        activity.startActivity(Intent.createChooser(imageIntent, title));
    }
    public static void shareImages(Activity activity, ArrayList<Uri> imageUris, String title){
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(mulIntent,title));
    }

}