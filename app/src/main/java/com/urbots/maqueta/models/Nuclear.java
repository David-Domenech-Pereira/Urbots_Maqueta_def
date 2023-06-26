package com.urbots.maqueta.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class Nuclear extends  ElementCiutat{

    private int potencia;
    private final int potencia_max = 255;
    public Nuclear(String ip, float energia, int potencia) {
        super(ip, energia);
        this.energia = energia;
        this.potencia = potencia;
    }
    @SuppressLint("Range")
    public  static  Nuclear getNuclear(){
        // Realiza la consulta en la base de datos
        String[] projection = {"ip", "energia","potencia"};
        Cursor cursor = database.query("Nuclear", projection, null, null, null, null, null);
        String ip="";
        int energia=0;
        int potencia=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ip = cursor.getString(cursor.getColumnIndex("ip"));
                energia = cursor.getInt(cursor.getColumnIndex("energia"));
                potencia = cursor.getInt(cursor.getColumnIndex("potencia"));
                // Procesar los valores recuperados de la consulta
            } while (cursor.moveToNext());

            cursor.close();
        }
        return new Nuclear(ip,energia,potencia);
    }

    @Override
    public String getCharFrame() {
        return "N";
    }

    @Override
    public String generateFrame() {
        String frame = "N|"; //comença amb N
        frame += getFrameEnabled()+"|"; //posem els enabled, en aquest cas 100000
        //Posem 5 cops la poténcia
        for(int i = 0; i <3; i++){
            frame += potencia+"|";
        }
        frame += getkWh()+"|";
        frame += getWh()+"|";
        return  frame;
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();
        values.put("potencia", potencia);
        values.put("energia",energia);

        // Parámetros de ejemplo para la cláusula WHERE
        String whereClause = "ip = ?";
        String[] whereArgs = {this.ip};

        // Realiza el update en la tabla
        database.update("Nuclear", values, whereClause, whereArgs);
    }

    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("potencia", potencia);
        values.put("energia",energia);
        database.insert("Nuclear", null, values);
    }

    @Override
    public void reclaculateEnergy() {
        float coeficient = potencia/potencia_max;

        energia = (500*10^6)*coeficient; //Fem fórmula segons consta al word
    }

    public int getPotencia() {
        return  potencia;
    }

    public void setPotencia(int i) {
        potencia = i;
        sendM();
        reclaculateEnergy();
    }

    public int getMaxPotencia() {
        return potencia_max;
    }
}
