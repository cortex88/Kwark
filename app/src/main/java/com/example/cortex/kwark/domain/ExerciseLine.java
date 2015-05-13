package com.example.cortex.kwark.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cortex on 03/05/2015.
 */
@DatabaseTable(tableName = "ExerciseLine")
public class ExerciseLine implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Routine routine;
    @DatabaseField
    private String name;
    @DatabaseField
    private String muscle;
    @DatabaseField
    private int sets;
    @DatabaseField
    private int minReps;
    @DatabaseField
    private int maxReps;

    public ExerciseLine() {
        this("", "");
    }

    public ExerciseLine(String name, String muscle) {
        this(name, muscle, 0, 0, 0);
    }
    /*
    public ExerciseLine(Exercise exercise, int sets, int minReps, int maxReps) {
        this(exercise.getName(), exercise.getMuscle(), sets, minReps, maxReps);
    }
    */

    public ExerciseLine(Routine routine) {
        this.routine = routine;
        this.name = "";
        this.muscle = "";
        this.sets = 4;
        this.minReps = 8;
        this.maxReps = 12;
    }

    public ExerciseLine(String name, String muscle, int sets, int minReps, int maxReps) {
        this.name = name;
        this.muscle = muscle;
        this.sets = 4;
        this.minReps = 8;
        this.maxReps = 12;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getMinReps() {
        return minReps;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    @Override
    public String toString() {
        return name + ", " + muscle + ", " + sets + ", [" + minReps + " .. " + maxReps + "]";
    }
}