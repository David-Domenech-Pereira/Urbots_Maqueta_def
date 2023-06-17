package com.urbots.maqueta.models;

public class Solar extends  ElementCiutat{
    int posicio;

    float energia;
    public  Solar(String ip, float energia, int posicio){
        super(ip,energia);
        this.energia = energia;
        this.posicio = posicio;

    }
    public static Solar getsolar(){
       //TODO read from DB
        return new Solar("192.168.0.10",0,40);
    }
}
