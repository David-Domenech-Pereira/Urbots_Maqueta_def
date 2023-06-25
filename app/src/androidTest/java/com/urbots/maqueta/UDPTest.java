package com.urbots.maqueta;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertTrue;

import android.database.sqlite.SQLiteDatabase;

import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.ElementCiutat;
import com.urbots.maqueta.models.Solar;

import org.junit.Test;

public class UDPTest {
    @Test
    public  void TestFrame(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
       //Creem un objecte Solar
        Solar element = Solar.getsolar();
        //Activem el 1 i el 2
        element.setStatus(true,0);
        element.setStatus(true,1);
        //La resta desactivem
        element.setStatus(false,2);
        element.setStatus(false,3);
        element.setStatus(false,4);
        element.setPosicio(123);
        //Miramos que se ponga 1100 0000 en el frame enabled y sea d e8
        assertTrue(element.getFrameEnabled().length()==8);
        assertTrue(element.getFrameEnabled().equals("11000000"));
        assertTrue(element.translatePosicio()>30);
        //Miramos que el frame sea S|11000000|123|123|123|123|123|
        assertTrue(element.generateFrame().equals("S|11000000|123|123|123|123|123|"));
    }
}
