package com.example.toyka_app;

public interface UdpOutputInterface extends GenericInterface{
    static byte NO_DATA_HEADER = (byte) 255;
    static byte STICK_DIRECTION_HEADER = 50;
    static byte TARGET_SPEED_HEADER = 51;

    void send_speed(byte speed);
    void send_direction(byte direction);


}