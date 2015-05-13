package com.example.cortex.kwark;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cortex.kwark.DAL.DaoFactory;
import com.example.cortex.kwark.domain.Exercise;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * Created by cortex on 03/05/2015.
 *
 * Also used to create an exercise.
 */
public class ExerciseEdit extends Activity {
    private static String KEY_EXERCISE = "Exercise";
    private EditText nameText;
    private EditText muscleText;
    private DaoFactory daoFactory;
    private Exercise exercise; // The current exercise being edited.
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_edit);
        nameText = (EditText) findViewById(R.id.name);
        muscleText = (EditText) findViewById(R.id.muscle);
        daoFactory = new DaoFactory(this);
        // Initialize exercise entity.
        if (savedInstanceState != null) {
            exercise = (Exercise) savedInstanceState.getSerializable(KEY_EXERCISE);
        }
        else {
            Bundle extras = getIntent().getExtras();
            Object passedExercise = extras.getSerializable(KEY_EXERCISE);
            if (passedExercise != null) {
                exercise = (Exercise)passedExercise;
            }
            else {
                exercise = new Exercise(); // Here, this activity was used to add a new exercise.
            }
        }
        updateGUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_EXERCISE, exercise);
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
        String muscle = muscleText.getText().toString();
        exercise.setName(name);
        exercise.setMuscle(muscle);
        RuntimeExceptionDao<Exercise, Integer> dao = daoFactory.getExerciseRuntimeExceptionDao();
        dao.createOrUpdate(exercise);
    }

    private void updateGUI() {
        nameText.setText(exercise.getName());
        muscleText.setText(exercise.getMuscle());
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