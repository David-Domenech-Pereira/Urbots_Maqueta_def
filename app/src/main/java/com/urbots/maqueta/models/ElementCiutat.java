package com.urbots.maqueta.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public abstract class ElementCiutat {

    protected int num_enabled; //Cantidad de elementos encendidos
    protected static SQLiteDatabase database;
    String ip;
    protected float energia; //Tenim la potencia en W
    ElementInteractuar elements[]; //TODO Seria millor implementar una llista dinàmica (sobre un hashmap) amb els actius
    int elements_num;
    protected double hora; //guardem hora, que són nombre de segons desde les 00
    public  ElementCiutat(String ip, float energia){
        this.ip = ip;
        this.energia = energia;
        elements = new ElementInteractuar[15];
        num_enabled = 0; //al principio no hay ninguno encendido
        loadElements();
        sendM();
    }


    public void loadElements(){

        String selection = "ciutat = ?";
        String[] selectionArgs = {ip};
        String[] projection = {"id","potEncendre", "enabled"};
        Cursor cursor = database.query("ElementInteractuar", projection, selection, selectionArgs, null, null, null);
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
                if(enabled) num_enabled++;
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
        //Si lo encendemos lo contamos, si lo apagamos lo descontamos
        if(isChecked) num_enabled++;
        else num_enabled--;
        elements[i].setEnabled(isChecked);

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
            if(i>=7){
                break; //Ja no n'hem de posar més
            }
            esc++;
        }
        if(esc < 7){
            while (esc<8){
                frame+="0";
                esc++;
            }
        }
        return  frame;
    }

    /**
     * Devuelve el carácter que correspondería en el Frame
     * @return Caràcter, per exemple 'S' per solar
     */
    public abstract String getCharFrame();
    /**
     * Métode que envia el missatge a la maqueta
     */
    public void sendM(){

        reclaculateEnergy();
        sendFrame(generateFrame(),ip);
        System.out.println(generateFrame());
        //Hem de enviar al master
        String frame_master = getCharFrame()+"|"+getFrameEnabled()+"|0|0|0|"+getkWh()+"|"+getWh()+"|";
        sendFrame(generateFrame(),"192.168.0.100");
    }
    protected int getkWh(){
        return (int) energia/(1000); //arrodonim i treiem només els KW
    }
    protected int getWh(){

        return (int) energia- getkWh()*1000; //Treiem els kW
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
    public void sendFrame(String frame, String ip){
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

    public void setIP(String s) {
        this.ip = s;
    }

    public void setStatusNoM(int i, boolean status) {
        if(status) num_enabled++;
        else num_enabled--;
        elements[i].setEnabled(status);
    }
}
