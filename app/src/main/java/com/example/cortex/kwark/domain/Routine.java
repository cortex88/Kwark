package com.example.cortex.kwark.domain;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cortex on 03/05/2015.
 */
@DatabaseTable(tableName = "Routine")
public class Routine implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String day;
    @ForeignCollectionField
    private ForeignCollection<ExerciseLine> exercises;

    public Routine() {
        this("");
    }

    public Routine(String name) {
        this(name, "");
    }

    public Routine(String name, String day) {
        this.name = name;
        this.day = day;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ForeignCollection<ExerciseLine> getExercises() {
        return exercises;
    }

    public void setExercises(ForeignCollection<ExerciseLine> exercises) {
        this.exercises = exercises;
    }

    public void addAnExerciseLine() {
        this.exercises.add(new ExerciseLine("BENCH", "CHEST", 4, 8, 12));
    }

    /**
     * @return All the exercises of this routine a string.
     */
    public String getExercisesString() {
        String returnString = "";
        if (exercises.isEmpty()) {
            return "No exercises added!";
        }
        for (ExerciseLine e : exercises) {
            returnString += e.toString() + "\n";
        }
        return returnString;
    }

    @Override
    public String toString() {
        return name + ", " + day;
    }
}