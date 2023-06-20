package com.urbots.maqueta.models;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class Solar extends  ElementCiutat{
    int posicio;

    float energia;
    public  Solar(String ip, float energia, int posicio){
        super(ip,energia);
        this.energia = energia;
        this.posicio = posicio;

    }
    public static Solar getsolar(){
        // Realiza la consulta en la base de datos
        String[] projection = {"ip", "energia","posicio"};
        Cursor cursor = database.query("Solar", projection, null, null, null, null, null);
        String ip="";
        int energia=0;
        int posicio=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ip = cursor.getString(cursor.getColumnIndex("ip"));
                energia = cursor.getInt(cursor.getColumnIndex("energia"));
                posicio = cursor.getInt(cursor.getColumnIndex("posicio"));
                // Procesar los valores recuperados de la consulta
            } while (cursor.moveToNext());

            cursor.close();
        }
        return new Solar(ip,energia,posicio);
    }


    public void setPosicio(int i) {
        posicio = i;
        sendM();
        update();
    }

    public int getPosicio() {
        return  posicio;
    }
    public int translatePosicio(){

    }
    @Override
    public String generateFrame() {
        String frame = "S|"; //comença amb 0
        frame += getFrameEnabled()+"|"; //posem els enabled
        //Posem 5 cops el número
        for(int i = 0; i <5; i++){
            frame += posicio+"|";
        }
        return  frame;
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();
        values.put("posicio", posicio);
        values.put("energia",energia);

        // Parámetros de ejemplo para la cláusula WHERE
        String whereClause = "ip = ?";
        String[] whereArgs = {this.ip};

        // Realiza el update en la tabla
        database.update("Solar", values, whereClause, whereArgs);
    }

    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("posicio", posicio);
        values.put("energia",energia);
        database.insert("Solar", null, values);
    }

    @Override
    public void reclaculateEnergy() {
        //TODO fer
    }
}
