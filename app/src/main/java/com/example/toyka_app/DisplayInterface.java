package com.example.toyka_app;

import android.graphics.Canvas;

public interface DisplayInterface extends GenericInterface{

    public void drawAll();
    public void updateUPS(double ups);

    public void setJoystickLocation(double x, double y);

    public void updateDebugConsole(String debug, int line);
}
