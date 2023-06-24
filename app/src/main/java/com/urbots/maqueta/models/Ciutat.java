package com.urbots.maqueta.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class Ciutat extends  ElementCiutat{


    public Ciutat(String ip, float energia) {
        super(ip, energia);
    }

    @Override
    public String getCharFrame() {
        return "C";
    }

    @Override
    public String generateFrame() {
        String frame = "C|"; //comença amb 0
        frame += getFrameEnabled()+"|"; //posem els enabled
        //Posem 5 cops el número
        for(int i = 0; i <5; i++){
            frame += energia+"|";
        }
        return  frame;
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();

        values.put("energia",energia);

        // Parámetros de ejemplo para la cláusula WHERE
        String whereClause = "ip = ?";
        String[] whereArgs = {this.ip};

        // Realiza el update en la tabla
        database.update("Ciutat", values, whereClause, whereArgs);
    }

    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("energia",energia);
        database.insert("Ciutat", null, values);
    }

    @Override
    public void reclaculateEnergy() {
        energia = 0;
        //Considerem que la industria és el primer element
        //Mirem si estem entre les 06 i les 23 hores
        if(hora>=(6*3600)&&hora<=(23*3600)){
            //200 MW la industria
            if(elements[0].getEnabled()){
                energia+=200*10^6; //li sumem els 200 MW
            }
            //10 MW cada casa
            for(int i = 1; i < elements_num; i++){
                if(elements[i].getEnabled()){
                    energia+=10*10^6; //li sumem els 10 MW
                }
            }
        }else{
            //100 MW la industria
            if(elements[0].getEnabled()){
                energia+=100*10^6; //li sumem els 100 MW
            }
            //5 MW cada casa
            for(int i = 1; i < elements_num; i++){
                if(elements[i].getEnabled()){
                    energia+=5*10^6; //li sumem els 5 MW
                }
            }
        }



    }

    @SuppressLint("Range")
    public static Ciutat getCiutat() {
        // Realiza la consulta en la base de datos
        String[] projection = {"ip", "energia"};
        Cursor cursor = database.query("Ciutat", projection, null, null, null, null, null);
        String ip="";
        int energia=0;
        int vent=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ip = cursor.getString(cursor.getColumnIndex("ip"));
                energia = cursor.getInt(cursor.getColumnIndex("energia"));

                // Procesar los valores recuperados de la consulta
            } while (cursor.moveToNext());

            cursor.close();
        }
        return new Ciutat(ip,energia);
    }
}
