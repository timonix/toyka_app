package com.example.toyka_app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Toyka_UDP implements UdpInputInterface,UdpOutputInterface{
    byte [] IP = {(byte) 192, (byte) 168,4,1};
    boolean started = false;
    private static Toyka_UDP inst;

    //input variables
    String[] debugStrings = {"line 0","line 1","line 2","line 3"};
    private DatagramSocket dsocket;

    private Toyka_UDP(){

    }

    public static Toyka_UDP getIo() {
        if (inst == null){
            inst = new Toyka_UDP();
        }
        return inst;
    }

    @Override
    public boolean interfaceStarted() {
        return started;
    }

    @Override
    public void send_direction(byte direction){
        byte[] buffer = {STICK_DIRECTION_HEADER, direction};
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

    @Override
    public void send_speed(byte speed) {
        byte[] buffer = {TARGET_SPEED_HEADER, speed};
        try{
            InetAddress address = InetAddress.getByAddress(IP);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 42100
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("");
        }

    }

    @Override
    public String debug(int i) {
        return debugStrings[i];
    }

    @Override


    public void handleIncomingPacket() {
        try {

            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            dsocket.setSoTimeout(1);
            dsocket.receive(packet);
            System.out.println("YAY");
            buffer[packet.getLength()]='\0';
            if (buffer[0]==DEBUG_HEADER){
                debugStrings[buffer[1]%4] = new String(buffer, StandardCharsets.UTF_8);
            }


        } catch (SocketTimeoutException e) {
            //System.err.println(e);
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startSocket() {
        try {
            if (!started){
                dsocket = new DatagramSocket(14201);
                started = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
