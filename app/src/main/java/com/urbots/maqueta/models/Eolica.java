package com.urbots.maqueta.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class Eolica extends  ElementCiutat{
    int vent;

    public Eolica(String ip, float energia, int vent) {
        super(ip, energia);
        this.vent = vent; //posem el vent que correspongui
    }
    @SuppressLint("Range")
    public  static  Eolica getEolica(){
        // Realiza la consulta en la base de datos
        String[] projection = {"ip", "energia","vent"};
        Cursor cursor = database.query("Eolica", projection, null, null, null, null, null);
        String ip="";
        int energia=0;
        int vent=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ip = cursor.getString(cursor.getColumnIndex("ip"));
                energia = cursor.getInt(cursor.getColumnIndex("energia"));
                vent = cursor.getInt(cursor.getColumnIndex("vent"));
                // Procesar los valores recuperados de la consulta
            } while (cursor.moveToNext());

            cursor.close();
        }
        return new Eolica(ip,energia,vent);
    }

    @Override
    public String getCharFrame() {
        return "E";
    }

    @Override
    public String generateFrame() {
        String frame = "E|"; //comença amb 0
        frame += getFrameEnabled()+"|"; //posem els enabled
        //Posem 5 cops el número
        for(int i = 0; i <3; i++){
            frame += vent+"|";
        }
        frame += getkWh()+"|";
        frame += getWh()+"|";
        return  frame;
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();
        values.put("vent", vent);
        values.put("energia",energia);

        // Parámetros de ejemplo para la cláusula WHERE
        String whereClause = "ip = ?";
        String[] whereArgs = {this.ip};

        // Realiza el update en la tabla
        database.update("Eolica", values, whereClause, whereArgs);
    }

    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("vent", vent);
        values.put("energia",energia);
        database.insert("Eolica", null, values);
    }

    /**
     * Funció que converteix la velocitat del vent a m/s, entre 5,5 i 22,2 m/s
     * @return float
     */
    private double convertWind(){
        //fem una regla de tres
        return  (vent*((22.2-5.5)/vent)+5.5);
    }
    @Override
    public void reclaculateEnergy() {
        //Per a més info consultar el word
        energia = (float) (num_enabled*0.5*1.2*75*convertWind());
    }

    public int getVent() {
        return  vent;
    }

    public void setVent(int i) {
        vent = i;
        sendM();
        update();
    }
}
