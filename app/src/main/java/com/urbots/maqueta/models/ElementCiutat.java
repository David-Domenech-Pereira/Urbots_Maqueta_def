package com.urbots.maqueta.models;

public abstract class ElementCiutat {
    String ip;
    float energia;
    ElementInteractuar elements[];
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
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
}
