package com.urbots.maqueta.models;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class ElementCiutat {
    String ip;
    float energia;
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
        //TODO Borrar aquestes carregues temporals
        elements[0] = new ElementInteractuar(this,true,true);
        elements[1]= new ElementInteractuar(this,true,true);
        elements[2]= new ElementInteractuar(this,true,true);
        elements[3]= new ElementInteractuar(this,true,true);

        elements_num = 4;
    }
    public  int getSizeElements(){
        return  elements_num;
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
    public void setStatus(boolean isChecked, int i) {
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
}
