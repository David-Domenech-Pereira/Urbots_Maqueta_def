package com.urbots.maqueta.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public abstract class ElementCiutat {
    protected static SQLiteDatabase database;
    String ip;
    protected float energia;
    ElementInteractuar elements[]; //TODO Seria millor implementar una llista dinàmica (sobre un hashmap) amb els actius
    int elements_num;
    public  ElementCiutat(String ip, float energia){
        this.ip = ip;
        this.energia = energia;
        elements = new ElementInteractuar[5];
        loadElements();
    }
    public void loadElements(){
        //TODO Carrega els elements de la BBDD
        String selection = "ciutat = ?";
        String[] selectionArgs = {ip};
        String[] projection = {"id","potEncendre", "enabled"};
        Cursor cursor = database.query("ElementCiutat", projection, selection, selectionArgs, null, null, null);
        int i = 0;
        elements_num=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {

                boolean potEncendre = cursor.getInt(cursor.getColumnIndex("potEncendre"))==1; //si és 1 está encés
                boolean enabled = cursor.getInt(cursor.getColumnIndex("enabled"))==1; //si és 1 está encés
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                // Procesar los valores recuperados de la consulta
                elements[i] = new ElementInteractuar(id,this,potEncendre,enabled);
                i++;
                elements_num++;
            } while (cursor.moveToNext());

            cursor.close();
        }

    }
    public  int getSizeElements(){
        return  elements_num;
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
    public void setStatus(boolean isChecked, int i) {
        elements[i].setEnabled(isChecked);
        reclaculateEnergy();
        sendM();
    }
    public String getFrameEnabled(){
        String frame = "";
        int esc=0;
        for(int i = 0; i < elements_num; i++){
            if(elements[i].getEnabled()){
                frame+="1"; //Si està encés posem un 1, altrament un 0
            }else{
                frame+="0";
            }
            if(i>=9){
                break; //Ja no n'hem de posar més
            }
            esc++;
        }
        if(esc < 8){
            while (esc<8){
                frame+="0";
                esc++;
            }
        }
        return  frame;
    }

    /**
     * Métode que envia el missatge a la maqueta
     */
    public void sendM(){
        sendFrame(generateFrame());
    }

    /**
     * Métode que genera el frame per aquest tipus
     * @return String Frame
     */
    public abstract String generateFrame();

    /**
     * Métode que envia el frame
     * @param frame String
     */
    public void sendFrame(String frame){
        new UDPSenderTask(ip,frame).execute();
    }
    /**
     * Método para poner la BBDD estática a todos los elementos
     * @param db BBDD a poner
     */
    public static void setDB(SQLiteDatabase db){
        database=db; //ponemos la BBDD
    }

    /**
     * Método que actualiza la BBDD
     */
    public abstract void update();

    /**
     * Método que hace un insert en la BBDD
     */
    public abstract void save();

    /**
     * Acció que recalcula la Energia
     */
    public abstract void reclaculateEnergy();
    public String getIP(){
        return  ip;
    }

}
