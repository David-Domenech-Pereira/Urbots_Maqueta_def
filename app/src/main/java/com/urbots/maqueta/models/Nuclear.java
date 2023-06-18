package com.urbots.maqueta.models;

public class Nuclear extends  ElementCiutat{
    float energia;

    public Nuclear(String ip, float energia) {
        super(ip, energia);
    }

    @Override
    public String generateFrame() {
        return null;
    }
}
