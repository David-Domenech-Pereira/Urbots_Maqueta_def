package com.urbots.maqueta.models;

public class Ciutat extends  ElementCiutat{
    float energia;

    public Ciutat(String ip, float energia) {
        super(ip, energia);
    }

    @Override
    public String generateFrame() {
        return null;
    }
}
