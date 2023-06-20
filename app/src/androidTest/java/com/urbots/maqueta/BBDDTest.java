package com.urbots.maqueta;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.*;

import static org.junit.Assert.assertTrue;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;



public class BBDDTest {
    @Test
    public  void TestArray(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
        Solar elemento = Solar.getsolar();

        assertTrue(elemento.getIP().equals("192.168.0.10"));
        assertTrue(elemento.getSizeElements()==5);

        //Ahora canviamos la IP y ponemos la "192.168.0.8", miramos si està OK
        elemento.setIP("192.168.0.8");
        elemento.update();
        assertTrue(elemento.getIP().equals("192.168.0.8"));
        //Lo dejamos cómo estaba
        elemento.setIP("192.168.0.10");
        elemento.update();
    }
    @Test
    public void testDatabaseCreation() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
        // Verificar si la tabla "Solar" existe en la base de datos
        boolean tableExists = checkIfTableExists("ElementCiutat",db);
        assertTrue(tableExists);
    }

    private boolean checkIfTableExists(String tableName,SQLiteDatabase database) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        Cursor cursor = database.rawQuery(query, null);
        boolean tableExists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return tableExists;
    }
}
