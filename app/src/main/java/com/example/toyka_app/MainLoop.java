package com.example.toyka_app;

import java.util.ArrayList;
import java.util.List;

public class MainLoop extends Thread {
    List<GenericInterface> interfaceList = new ArrayList<>();
    private boolean isRunning = true;

    public void addInterface(GenericInterface io) {
        interfaceList.add(io);
    }


    @Override
    public void run() {
        super.run();

        while (isRunning){
            String debug_0 = "";
            String debug_1 = "";
            String debug_2 = "";
            String debug_3 = "";
            //get inputs
            for(GenericInterface i :interfaceList){
                if (i instanceof UdpInputInterface){
                    ((UdpInputInterface) i).startSocket();
                }
                if (i instanceof UdpInputInterface && i.interfaceStarted()){

                    ((UdpInputInterface) i).handleIncomingPacket();
                    debug_0 = ((UdpInputInterface) i).debug(0);
                    debug_1 = ((UdpInputInterface) i).debug(1);
                    debug_2 = ((UdpInputInterface) i).debug(2);
                    debug_3 = ((UdpInputInterface) i).debug(3);
                }
            }

            //calculate logic
            //set outputs
            for(GenericInterface i :interfaceList){
                if (i instanceof DisplayInterface && i.interfaceStarted()){
                    ((DisplayInterface) i).updateUPS(41.9);
                    ((DisplayInterface) i).updateFPS(125);
                    ((DisplayInterface) i).updateDebugConsole(debug_0,0);
                    ((DisplayInterface) i).updateDebugConsole(debug_1,1);
                    ((DisplayInterface) i).updateDebugConsole(debug_2,2);
                    ((DisplayInterface) i).updateDebugConsole(debug_3,3);
                    ((DisplayInterface) i).drawAll();
                }

                if (i instanceof UdpOutputInterface && i.interfaceStarted()){
                    ((UdpOutputInterface) i).send_speed((byte) 100);
                    ((UdpOutputInterface) i).send_direction((byte)5);

                }
            }

        }
    }
}
