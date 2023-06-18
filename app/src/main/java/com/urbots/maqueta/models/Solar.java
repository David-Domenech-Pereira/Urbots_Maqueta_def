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


    public void setPosicio(int i) {
        posicio = i;
        sendM();
    }

    public int getPosicio() {
        return  posicio;
    }

    @Override
    public String generateFrame() {
        String frame = "S|"; //comença amb 0
        frame += getFrameEnabled()+"|"; //posem els enabled
        //Posem 5 cops el número
        for(int i = 0; i <5; i++){
            frame += posicio+"|";
        }
        return  frame;
    }
}
