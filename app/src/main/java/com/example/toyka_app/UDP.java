package com.example.toyka_app;

import android.graphics.Canvas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.ThreadPoolExecutor;

public class UDP{
    private final LOGGER logger = LOGGER.getLogger(this.getClass());
    byte [] IP = {(byte) 192, (byte) 168,4,1};
    boolean started = false;
    private static UDP inst;
    private byte battery_level = 0;

    static byte ACCELEROMETER_HEADER = 80;
    static byte GYROSCOPE_HEADER = 81;
    static byte BATTERI_HEADER = 82;
    static byte DEBUG_HEADER = 100;
    static byte NO_DATA_HEADER = (byte) 255;
    static byte STICK_DIRECTION_HEADER = 50;
    static byte TARGET_SPEED_HEADER = 51;

    //input variables
    private String[] debugStrings = {"line 0","line 1","line 2","line 3"};
    private DatagramSocket dsocket;

    private UDP(){
        new Thread(() -> {
            while(true){
                handleIncomingPacket();
            }
        }).start();
    }

    public static UDP getIo() {
        if (inst == null){
            inst = new UDP();
        }
        return inst;
    }


    public boolean interfaceStarted() {
        return started;
    }

    public void send_direction(byte direction){

        byte[] buffer = {STICK_DIRECTION_HEADER, direction};
        try{
            InetAddress address = InetAddress.getByAddress(IP);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 42100
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
            datagramSocket.close();

        } catch(Exception e){
            e.printStackTrace();
            debugStrings[3] = e.getMessage();
            System.err.println("");
        }


    }

    public void send_speed(byte speed) {

        byte[] buffer = {TARGET_SPEED_HEADER, speed};
        try{
            InetAddress address = InetAddress.getByAddress(IP);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 42100
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
            datagramSocket.close();
        } catch(Exception e){
            e.printStackTrace();
            debugStrings[3] = e.getMessage();
            System.err.println("");

        }
    }


    public String debug(int i) {
        return debugStrings[i];
    }


    public byte batteryLevel() {
        return battery_level;
    }


    public GyroData getGyroData() {
        return null;
    }


    public void handleIncomingPacket() {
        if(!started){
            return;
        }
        try {

            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            dsocket.setSoTimeout(1000);

            dsocket.receive(packet);

            System.out.println("YAY");
            buffer[packet.getLength()]='\0';
            if (buffer[0]==DEBUG_HEADER){
                debugStrings[buffer[1]%4] = new String(buffer, StandardCharsets.UTF_8);
            }else if(buffer[0]==BATTERI_HEADER){
                battery_level = buffer[1];
            }



        } catch (SocketTimeoutException e) {
            //System.err.println(e);
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startSocket() {
        try {
            if (!started){
                InetAddress addr = getAddress();
                dsocket = new DatagramSocket(14201,addr);
                started = true;
                System.out.println("start socket");
                System.out.println(dsocket.getLocalAddress().getHostAddress());
                System.out.println(dsocket.getLocalPort());

            }


        } catch (Exception e) {
            System.err.println(e);
            debugStrings[3] = e.getMessage();
            e.printStackTrace();
        }
    }

    private InetAddress getAddress() throws SocketException {
        Enumeration ae = NetworkInterface.getNetworkInterfaces();
        while(ae.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) ae.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.getAddress()[0]==(byte)192) return i;
            }
        }
        return null;
    }
}
