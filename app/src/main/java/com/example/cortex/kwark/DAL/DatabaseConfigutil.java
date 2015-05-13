package com.example.cortex.kwark.DAL;

import com.example.cortex.kwark.domain.Exercise;
import com.example.cortex.kwark.domain.ExerciseLine;
import com.example.cortex.kwark.domain.Routine;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This class will create the configuration files.
 * On database update, execute this once, and copy-paste the ormlite_config.txt from the
 * root raw folder to the app raw folder.
 *
 * Created by cortex on 03/05/2015.
 */
public class DatabaseConfigutil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{
            Exercise.class,
            ExerciseLine.class,
            Routine.class
    };

    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile("ormlite_config.txt", classes);
    }
}