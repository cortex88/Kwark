package com.example.cortex.kwark.DAL;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.cortex.kwark.domain.Exercise;
import com.example.cortex.kwark.domain.ExerciseLine;
import com.example.cortex.kwark.domain.Routine;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;

/**
 * Created by cortex on 04/05/2015.
 */
public class DaoFactory {
    private SharedPreferences preferences;
    private DatabaseHelper databaseHelper = null;
    private Dao<Exercise, Integer> exerciseDao = null;
    private Dao<Routine, Integer> routineDao = null;
    private Dao<ExerciseLine, Integer> exerciseLineDao = null;
    private RuntimeExceptionDao<Exercise, Integer> exerciseRuntimeExceptionDao = null;
    private RuntimeExceptionDao<Routine, Integer> routineRuntimeExceptionDao = null;
    private RuntimeExceptionDao<ExerciseLine, Integer> exerciseLineRuntimeExceptionDao = null;

    public DaoFactory(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        databaseHelper = new DatabaseHelper(context);
    }

    public Dao<Exercise, Integer> getExerciseDao() {
        if (exerciseDao == null) {
            try {
                exerciseDao = databaseHelper.getDao(Exercise.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exerciseDao;
    }

    public RuntimeExceptionDao<Exercise, Integer> getExerciseRuntimeExceptionDao() {
        if (exerciseRuntimeExceptionDao == null) {
            exerciseRuntimeExceptionDao = databaseHelper.getRuntimeExceptionDao(Exercise.class);
        }
        return exerciseRuntimeExceptionDao;
    }

    public Dao<Routine, Integer> getRoutineDao() {
        if (routineDao == null) {
            try {
                routineDao = databaseHelper.getDao(Routine.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return routineDao;
    }

    public RuntimeExceptionDao<Routine, Integer> getRoutineRuntimeExceptionDao() {
        if (routineRuntimeExceptionDao == null) {
            routineRuntimeExceptionDao = databaseHelper.getRuntimeExceptionDao(Routine.class);
        }
        return routineRuntimeExceptionDao;
    }

    public Dao<ExerciseLine, Integer> getExerciseLineDao() {
        if (exerciseLineDao == null) {
            try {
                exerciseLineDao = databaseHelper.getDao(ExerciseLine.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exerciseLineDao;
    }

    public RuntimeExceptionDao<ExerciseLine, Integer> getExerciseLineRuntimeExceptionDao() {
        if (exerciseLineRuntimeExceptionDao == null) {
            exerciseLineRuntimeExceptionDao = databaseHelper.getRuntimeExceptionDao(ExerciseLine.class);
        }
        return exerciseLineRuntimeExceptionDao;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
