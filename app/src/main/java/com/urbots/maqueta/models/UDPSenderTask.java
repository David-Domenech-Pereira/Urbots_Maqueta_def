package com.urbots.maqueta.models;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSenderTask extends AsyncTask<Void, Void, Void> {

    private static final int UDP_PORT = 7002;
    String ip;
    String frame;
    public  UDPSenderTask(String ip, String message){
        super();
        this.ip = ip;
        frame = message;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Create a DatagramSocket
            DatagramSocket socket = new DatagramSocket();

            // Specify the destination IP address and port number
            InetAddress IPAddress = InetAddress.getByName(ip);
            int port = UDP_PORT;

            // Create the message to send
            String message = frame;
            byte[] sendData = message.getBytes();

            // Create a DatagramPacket with the message, IP address, and port number
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            // Send the packet
            socket.send(packet);

            // Close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

