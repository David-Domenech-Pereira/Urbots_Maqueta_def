package com.urbots.maqueta.models;

public class Eolica extends  ElementCiutat{
    int vent;
    float energia;

    public Eolica(String ip, float energia) {
        super(ip, energia);
    }

    @Override
    public String generateFrame() {
        return null;
    }
}
