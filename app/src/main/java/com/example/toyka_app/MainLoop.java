package com.example.toyka_app;

import java.util.ArrayList;
import java.util.List;

public class MainLoop extends Thread {
    final Display disp;
    final UDP udp;

    private boolean isRunning = true;

    private long loopStartTime = System.nanoTime();
    private long smoothTime;


    private Joystick joy_speed;
    private Joystick joy_direction;

    public MainLoop(Display disp, UDP udp_io) {
        this.disp = disp;
        this.udp = udp_io;
    }


    @Override
    public void run() {
        super.run();

        while (isRunning){
            //get inputs

            udp.startSocket();
            String debug_0 = udp.debug(0);
            String debug_1 = udp.debug(1);
            String debug_2 = udp.debug(2);
            String debug_3 = udp.debug(3);



            //calculate logic
            long elapsed = System.nanoTime() - loopStartTime;
            loopStartTime = System.nanoTime();
            smoothTime = (smoothTime*9+elapsed)/10;


            // Joystick stuff
            byte speedSignal = 0;    // 100 is default value (middle of 0 and 200)
            byte directionSignal = 0;

            if (joy_speed != null){
                speedSignal = joy_speed.calculateSignal();
            }

            if (joy_direction != null) {
                directionSignal = joy_direction.calculateSignal();
            }



            //set outputs

            joy_speed = disp.getJoyStickSpeed();
            joy_direction = disp.getJoyStickDirection();



            disp.updateUPS(1.0/(smoothTime*Math.pow(10,-9)));
            disp.updateDebugConsole(debug_0,0);
            disp.updateDebugConsole(debug_1,1);
            disp.updateDebugConsole(debug_2,2);
            disp.updateDebugConsole(debug_3,3);
            disp.updateBatteryIndicator(udp.batteryLevel()/256.0);
            disp.setJoystickLocation();

            udp.update_speed(speedSignal);
            udp.update_direction(directionSignal);

        }
    }
}
