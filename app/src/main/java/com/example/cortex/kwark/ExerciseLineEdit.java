package com.example.cortex.kwark;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cortex.kwark.DAL.DaoFactory;
import com.example.cortex.kwark.domain.Exercise;
import com.example.cortex.kwark.domain.ExerciseLine;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * Created by cortex on 03/05/2015.
 *
 * Also used to create an exercise line.
 */
public class ExerciseLineEdit extends Activity {
    private static String KEY_EXERCISE_LINE = "ExerciseLine";
    private EditText nameText;
    private EditText muscleText;
    private EditText setsText;
    private EditText minRepsText;
    private EditText maxRepsText;
    private DaoFactory daoFactory;
    private ExerciseLine exerciseLine; // The current ExerciseLine being edited
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_line_edit);
        nameText = (EditText) findViewById(R.id.name);
        muscleText = (EditText) findViewById(R.id.muscle);
        setsText = (EditText) findViewById(R.id.sets);
        minRepsText = (EditText) findViewById(R.id.minReps);
        maxRepsText = (EditText) findViewById(R.id.maxReps);
        daoFactory = new DaoFactory(this);
        // Initialize exerciseLine entity.
        if (savedInstanceState != null) {
            exerciseLine = (ExerciseLine) savedInstanceState.getSerializable(KEY_EXERCISE_LINE);
        }
        else {
            Bundle extras = getIntent().getExtras();
            Object passedExerciseLine = extras.getSerializable(KEY_EXERCISE_LINE);
            if (passedExerciseLine != null) {
                exerciseLine = (ExerciseLine)passedExerciseLine;
            }
            else {
                exerciseLine = new ExerciseLine(); // Here, this activity was used to add a new exerciseline.
            }
        }
        updateGUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_EXERCISE_LINE, exerciseLine);
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
        // Get values.
        String name = nameText.getText().toString();
        String muscle = muscleText.getText().toString();
        int sets = Integer.parseInt(setsText.getText().toString());
        int minReps = Integer.parseInt(minRepsText.getText().toString());
        int maxReps = Integer.parseInt(maxRepsText.getText().toString());
        // Set and save values.
        exerciseLine.setName(name);
        exerciseLine.setMuscle(muscle);
        exerciseLine.setSets(sets);
        exerciseLine.setMinReps(minReps);
        exerciseLine.setMaxReps(maxReps);
        RuntimeExceptionDao<ExerciseLine, Integer> dao = daoFactory.getExerciseLineRuntimeExceptionDao();
        dao.createOrUpdate(exerciseLine);
    }

    private void updateGUI() {
        nameText.setText(exerciseLine.getName());
        muscleText.setText(exerciseLine.getMuscle());
        setsText.setText(String.valueOf(exerciseLine.getSets()));
        minRepsText.setText(String.valueOf(exerciseLine.getMinReps()));
        maxRepsText.setText(String.valueOf(exerciseLine.getMaxReps()));
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                saveState();
                setResult(RESULT_OK);
                finish();
            case R.id.cancelButton:
                setResult(RESULT_CANCELED);
                finish();
        }
    }
}