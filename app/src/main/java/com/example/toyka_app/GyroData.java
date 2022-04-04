package com.example.toyka_app;

public class GyroData {
    public final float x;
    public final float y;
    public final float z;
    public final long time;

    public GyroData(float x, float y, float z, long time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }
}
