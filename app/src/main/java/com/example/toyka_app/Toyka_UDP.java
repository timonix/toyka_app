package com.example.toyka_app;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Toyka_UDP implements UdpInputInterface,UdpOutputInterface{


    @Override
    public boolean interfaceStarted() {
        return true;
    }

    @Override
    public void send_speed(byte speed) {
        byte[] buffer = {TARGET_SPEED_HEADER, speed};
        byte [] IP = {(byte) 192, (byte) 168,4,1};
        try{
            InetAddress address = InetAddress.getByAddress(IP);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 42100
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("");
        }

    }
}
