package com.example.cortex.kwark;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.cortex.kwark.DAL.DatabaseHelper;
import com.example.cortex.kwark.domain.Routine;
import com.j256.ormlite.android.apptools.OpenHelperManager;


public class Home extends Activity {
    private static final int EXERCISES_MENU_ID = Menu.FIRST;
    private static final int ROUTINES_MENU_ID = Menu.FIRST + 1;
    private static final int LOGS_MENU_ID = Menu.FIRST + 2;

    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        updateGUI();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateGUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EXERCISES_MENU_ID, 0, R.string.manage_exercises);
        menu.add(0, ROUTINES_MENU_ID, 0, R.string.manage_routines);
        menu.add(0, LOGS_MENU_ID, 0, R.string.manage_logs);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case EXERCISES_MENU_ID:
                manageExercises();
                return true;
            case ROUTINES_MENU_ID:
                manageRoutines();
                return true;
            case LOGS_MENU_ID:
                manageLogs();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateGUI();
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.exercise_button:
                manageExercises();
                return;
            case R.id.routine_button:
                manageRoutines();

                return;
            case R.id.log_button:
                manageLogs();
                return;
        }
    }

    private void updateGUI() {
    }

    private void manageExercises() {
        Intent i = new Intent(this, Exercises.class);
        startActivity(i);
    }

    private void manageRoutines() {
        Intent i = new Intent(this, Routines.class);
        startActivity(i);
    }

    private void manageLogs() {
        spawnWarningDialog("Workout log management not yet implemented!");
    }

    private void spawnWarningDialog(String message) {
        spawnAlertDialog("Warning", message);
    }

    private void spawnAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}