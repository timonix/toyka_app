package com.example.toyka_app;

public interface UdpInputInterface extends GenericInterface {
    static byte ACCELEROMETER_HEADER = 80;
    static byte GYROSCOPE_HEADER = 81;
    static byte BATTERI_HEADER = 82;
    static byte DEBUG_HEADER = 100;

    String debug(int i);

    public void startSocket();
}
