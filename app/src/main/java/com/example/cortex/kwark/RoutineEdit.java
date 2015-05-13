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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cortex.kwark.DAL.DaoFactory;
import com.example.cortex.kwark.domain.ExerciseLine;
import com.example.cortex.kwark.domain.Routine;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cortex on 03/05/2015.
 *
 * Also used to create a routine.
 */
public class RoutineEdit extends Activity {
    private static String KEY_ROUTINE = "Routine";
    private static String KEY_EXERCISE_LINE = "ExerciseLine";
    private static final int ACTIVITY_EDIT_EXERCISE_LINE = 0;
    private static final int HOME_MENU_ID = Menu.FIRST;
    private static final int BACK_TO_ROUTINES_MENU_ID = Menu.FIRST + 1;
    private static final int ADD_EXERCISE_LINE_MENU_ID = Menu.FIRST + 2;
    private static final int EDIT_EXERCISE_LINE_CONTEXT_MENU_ID = Menu.FIRST + 3;
    private static final int DELETE_EXERCISE_LINE_CONTEXT_MENU_ID = Menu.FIRST + 4;

    private EditText nameText;
    private EditText dayText;
    private TextView countLabel;
    private ListView exerciseListView;
    private DaoFactory daoFactory;
    private Routine routine; // The current routine being edited.
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_edit);
        nameText = (EditText) findViewById(R.id.name);
        dayText = (EditText) findViewById(R.id.day);
        countLabel = (TextView) findViewById(R.id.countLabel);
        exerciseListView = (ListView) findViewById(R.id.exerciseListView);
        daoFactory = new DaoFactory(this);
        // Initialize routine entity
        if (savedInstanceState != null) {
            routine = (Routine) savedInstanceState.getSerializable(KEY_ROUTINE);
        }
        else {
            Bundle extras = getIntent().getExtras();
            Object passedRoutine = extras.getSerializable(KEY_ROUTINE);
            if (passedRoutine != null) {
                routine = (Routine)passedRoutine;
            }
            else {
                routine = new Routine();
            }
        }
        registerForContextMenu(exerciseListView);
        addOnClickListener();
        updateGUI();
    }

    private void updateGUI() {
        nameText.setText(routine.getName());
        dayText.setText(routine.getDay());
        // Get all ExerciseLine's of the routine
        RuntimeExceptionDao<ExerciseLine, Integer> exerciseLineDao = daoFactory.getExerciseLineRuntimeExceptionDao();
        Map<String, Object> fieldValueMap = new HashMap<String, Object>();
        fieldValueMap.put("routine_id", routine.getId());
        List<ExerciseLine> exerciseLineList = exerciseLineDao.queryForFieldValues(fieldValueMap);
        // Display all ExerciseLine's
        final ArrayAdapter<ExerciseLine> adapter = new ArrayAdapter<ExerciseLine>(this,
                android.R.layout.simple_list_item_1, exerciseLineList);
        exerciseListView.setAdapter(adapter);
        int numberOfExercises = exerciseLineList.size();
        countLabel.setText("(" + String.valueOf(numberOfExercises) + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, HOME_MENU_ID, 0, R.string.home);
        menu.add(0, BACK_TO_ROUTINES_MENU_ID, 0, R.string.back_to_routines);
        menu.add(0, ADD_EXERCISE_LINE_MENU_ID, 0, R.string.add_exercise);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case HOME_MENU_ID:
                goHome();
                break;
            case BACK_TO_ROUTINES_MENU_ID:
                finish();
                break;
            case ADD_EXERCISE_LINE_MENU_ID:
                addExerciseLine();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_EXERCISE_LINE_CONTEXT_MENU_ID, 0, R.string.edit_exercise);
        menu.add(0, DELETE_EXERCISE_LINE_CONTEXT_MENU_ID, 0, R.string.delete_exercise);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Fetch the real ExerciseLine entity from given menu item
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ExerciseLine exerciseLine = (ExerciseLine)exerciseListView.getAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case EDIT_EXERCISE_LINE_CONTEXT_MENU_ID:
                editExerciseLine(exerciseLine);
                return true;
            case DELETE_EXERCISE_LINE_CONTEXT_MENU_ID:
                deleteExerciseLine(exerciseLine);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void goHome() {
        Intent i= new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    private void addExerciseLine() {
        editExerciseLine(new ExerciseLine(routine)); // Pass current routine (foreign key/object)
    }

    private void editExerciseLine(ExerciseLine exerciseLine) {
        Intent i = new Intent(this, ExerciseLineEdit.class);
        i.putExtra(KEY_EXERCISE_LINE, exerciseLine);
        startActivityForResult(i, ACTIVITY_EDIT_EXERCISE_LINE);
    }

    private void deleteExerciseLine(final ExerciseLine exerciseLine) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Delete the routine.
                        RuntimeExceptionDao<ExerciseLine, Integer> dao = daoFactory.getExerciseLineRuntimeExceptionDao();
                        dao.delete(exerciseLine);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ROUTINE, routine);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGUI();
    }

    private void saveState() {
        String name = nameText.getText().toString();
        String day = dayText.getText().toString();
        routine.setName(name);
        routine.setDay(day);
        // Save the routine.
        RuntimeExceptionDao<Routine, Integer> dao = daoFactory.getRoutineRuntimeExceptionDao();
        dao.createOrUpdate(routine);
        if (routine.getExercises() == null) {
            dao.assignEmptyForeignCollection(routine, "exercises");
        }
        // Get and save all ExerciseLine's of the routine.
        RuntimeExceptionDao<ExerciseLine, Integer> exerciseLineDao = daoFactory.getExerciseLineRuntimeExceptionDao();
        Map<String, Object> fieldValueMap = new HashMap<String, Object>();
        fieldValueMap.put("routine_id", routine.getId());
        List<ExerciseLine> exerciseLineList = exerciseLineDao.queryForFieldValues(fieldValueMap);
        for (ExerciseLine exercise : exerciseLineList) {
            exerciseLineDao.createOrUpdate(exercise);
        }
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.addExerciseButton:
                addExerciseLine();
                break;
            case R.id.confirmButton:
                saveState();
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.cancelButton:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void addOnClickListener() {
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Fetch and edit ExerciseLine entity
                ExerciseLine exerciseLine = (ExerciseLine)exerciseListView.getAdapter().getItem(position);
                editExerciseLine(exerciseLine);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateGUI();
    }
}