package com.urbots.maqueta.models;

public class ElementInteractuar {
    int id;
    ElementCiutat ciutat;
    boolean potEncendre;
    boolean enabled;
    public  ElementInteractuar(int id,ElementCiutat ciutat, boolean potEncendre, boolean enabled){
        this.ciutat = ciutat;
        this.potEncendre = potEncendre;
        this.enabled = enabled;
        this.id = id;
    }

    public void setEnabled(boolean isChecked) {
        enabled=isChecked;
    }

    public boolean getEnabled() {
        return  enabled;
    }


}
