package com.example.cortex.kwark.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by cortex on 04/05/2015.
 */
public class DialogSpawner {

    public static void spawnWarningDialog(String message, Context context) {
        SpawnAlertDialog("Warning", message, context);
    }

    public static void SpawnAlertDialog(String title, String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
