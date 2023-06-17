package com.urbots.maqueta.models;

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
        elements_num = 2;
    }
    public  int getSizeElements(){
        return  elements_num;
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
}
