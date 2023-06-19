package com.urbots.maqueta.auxiliar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maqueta_bd.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            executeSQLScript(db, "AppMaquetaBBDD.sql");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    private void executeSQLScript(SQLiteDatabase db, String scriptName) {
        try {
            InputStream inputStream = context.getAssets().open(scriptName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder script = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().endsWith(";")) {
                    script.append(line);
                    String sqlStatement = script.toString();
                    System.out.println("SQL->"+sqlStatement);
                    db.execSQL(sqlStatement);
                    script.setLength(0); // Reiniciar el StringBuilder para la siguiente sentencia SQL
                } else {
                    script.append(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Error executing SQL script", e);
        }
    }
}

