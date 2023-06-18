package com.urbots.maqueta.models;

public class ElementInteractuar {
    ElementCiutat ciutat;
    boolean potEncendre;
    boolean enabled;
    public  ElementInteractuar(ElementCiutat ciutat, boolean potEncendre, boolean enabled){
        this.ciutat = ciutat;
        this.potEncendre = potEncendre;
        this.enabled = enabled;
    }

    public void setEnabled(boolean isChecked) {
        enabled=isChecked;
    }

    public boolean getEnabled() {
        return  enabled;
    }
}
