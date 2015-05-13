package com.example.cortex.kwark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cortex.kwark.DAL.DaoFactory;
import com.example.cortex.kwark.domain.Exercise;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * List overview activity of all stored exercises.
 * Can initiate exercise CRUDs.
 *
 * Created by cortex on 03/05/2015.
 */
public class Exercises extends Activity {
    private static final String KEY_EXERCISE = "Exercise";
    private static final int ACTIVITY_EDIT_EXERCISE = 0;
    private static final int HOME_MENU_ID = Menu.FIRST;
    private static final int ADD_EXERCISE_MENU_ID = Menu.FIRST + 1;
    private static final int EDIT_EXERCISE_CONTEXT_MENU_ID = Menu.FIRST + 2;
    private static final int DELETE_EXERCISE_CONTEXT_MENU_ID = Menu.FIRST + 3;

    private ListView exerciseListView;
    private DaoFactory daoFactory;
    //private RuntimeExceptionDao<Exercise, Integer> dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises);
        daoFactory = new DaoFactory(this);
        //dao = (DaoFactory) getApplication().getExerciseRuntimeExceptionDao();
        exerciseListView = (ListView) findViewById(R.id.exerciseListView);
        registerForContextMenu(exerciseListView);
        addOnClickListener();
        updateGUI();
    }

    private void updateGUI() {
        // Get and display all exercises from db
        RuntimeExceptionDao<Exercise, Integer> dao = daoFactory.getExerciseRuntimeExceptionDao();
        List<Exercise> exercises = dao.queryForAll();
        final ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(this,
                android.R.layout.simple_list_item_1, exercises);
        exerciseListView.setAdapter(adapter);
        // Display exercise count
        TextView countText = (TextView) findViewById(R.id.countLabel);
        int count = (int) dao.countOf();
        countText.setText("(" + count + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, HOME_MENU_ID, 0, R.string.home);
        menu.add(0, ADD_EXERCISE_MENU_ID, 0, R.string.add_exercise);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case HOME_MENU_ID:
                goHome();
                break;
            case ADD_EXERCISE_MENU_ID:
                addExercise();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void goHome() {
        Intent i= new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_EXERCISE_CONTEXT_MENU_ID, 0, R.string.edit_exercise);
        menu.add(0, DELETE_EXERCISE_CONTEXT_MENU_ID, 0, R.string.delete_exercise);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Fetch the real Exercise entity from given menu item
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Exercise exercise = (Exercise)exerciseListView.getAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case EDIT_EXERCISE_CONTEXT_MENU_ID:
                editExercise(exercise);
                return true;
            case DELETE_EXERCISE_CONTEXT_MENU_ID:
                deleteExercise(exercise);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    private void addExercise() {
        editExercise(new Exercise());
    }

    private void editExercise(Exercise exercise) {
        Intent i = new Intent(this, ExerciseEdit.class);
        i.putExtra(KEY_EXERCISE, exercise);
        startActivityForResult(i, ACTIVITY_EDIT_EXERCISE);
    }
    /*
    private void deleteExercise(int id) {
        RuntimeExceptionDao<Exercise, Integer> dao = dbHelper.getExerciseRuntimeExceptionDao();
        Exercise exercise = dao.queryForId(id);
        deleteExercise(exercise);
    }
    */
    private void deleteExercise(final Exercise exercise) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Delete the exercise.
                        RuntimeExceptionDao<Exercise, Integer> dao = daoFactory.getExerciseRuntimeExceptionDao();
                        dao.delete(exercise);
                        updateGUI();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.addExerciseButton:
                addExercise();
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateGUI();
    }

    private void addOnClickListener() {
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Fetch and edit Exercise entity
                Exercise exercise = (Exercise)exerciseListView.getAdapter().getItem(position);
                editExercise(exercise);
            }
        });
    }
}

