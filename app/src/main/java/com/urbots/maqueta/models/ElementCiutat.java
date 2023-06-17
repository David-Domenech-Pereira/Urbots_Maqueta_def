package com.urbots.maqueta.models;

public abstract class ElementCiutat {
    String ip;
    float energia;
    ElementInteractuar elements[];
    public  ElementCiutat(String ip, float energia){
        this.ip = ip;
        this.energia = energia;
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
}
