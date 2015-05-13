package com.example.cortex.kwark.domain;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

import java.io.Serializable;

/**
 * Created by cortex on 03/05/2015.
 */
@DatabaseTable(tableName = "Exercise")
public class Exercise implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String muscle;

    public Exercise() {
        this.name = "";
        this.muscle = "";
    }

    public Exercise(String name, String muscle) {
        this.name = name;
        this.muscle = muscle;
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

    @Override
    public String toString() {
        return name + ", " + muscle;
    }
}