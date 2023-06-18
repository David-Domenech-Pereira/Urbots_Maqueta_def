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
        elements[2]= new ElementInteractuar(this,true,true);
        elements[3]= new ElementInteractuar(this,true,true);

        elements_num = 4;
    }
    public  int getSizeElements(){
        return  elements_num;
    }
    public ElementInteractuar[] getElements(){
        return  elements;
    }
    public void setStatus(boolean isChecked, int i) {
        elements[i].setEnabled(isChecked);
    }
    public String getFrameEnabled(){
        String frame = "";
        for(int i = 0; i < elements_num; i++){
            if(elements[i].getEnabled()){
                frame+="1"; //Si està encés posem un 1, altrament un 0
            }else{
                frame+="0";
            }
            if(i>=9){
                break; //Ja no n'hem de posar més
            }
        }
        return  frame;
    }
    public abstract String generateFrame();
    public void sendFrame(String frame){
        try {
            // Create a DatagramSocket
            DatagramSocket socket = new DatagramSocket();

            // Specify the destination IP address and port number
            InetAddress IPAddress = InetAddress.getByName(ip);
            int port = 7002;

            // Create the message to send
            String message = frame;

            // Convert the message to bytes
            byte[] sendData = message.getBytes();

            // Create a DatagramPacket with the message, IP address, and port number
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            // Send the packet
            socket.send(packet);

            // Close the socket
            socket.close();

            System.out.println("UDP frame sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
