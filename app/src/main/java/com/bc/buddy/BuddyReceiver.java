package com.bc.buddy;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.content.ClipData;

import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class BuddyReceiver extends BroadcastReceiver {
    private static final String TAG = "BuddyReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) action = "";

        if (action.equals("clipboard")) {
            ClipboardManager cb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            Log.d(TAG, "Setting text into clipboard");
            String text = intent.getStringExtra("text");
            if (text != null) {
                ClipData clipData = ClipData.newPlainText("text", text);
                cb.setPrimaryClip(clipData);
                setResultCode(Activity.RESULT_OK);
                setResultData("Text is copied into clipboard.");
            } else {
                setResultCode(Activity.RESULT_CANCELED);
                setResultData("No text is provided. Use -e text \"text to be pasted\"");
            }
        } else if (action.equals("wallpaper")) {
            Log.d(TAG, "Updating wallpaper");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            WallpaperManager wpm = WallpaperManager.getInstance(context);
            InputStream ins;
            try {
                ins = new URL("https://picsum.photos/2000/2000").openStream();
                wpm.setStream(ins);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Not supported action");
        }
    }
}
