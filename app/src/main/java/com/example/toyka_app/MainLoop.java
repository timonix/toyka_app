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

            //get inputs
            for(GenericInterface i :interfaceList){
                if (i instanceof UdpInputInterface && i.interfaceStarted()){

                }
            }

            //calculate logic
            //set outputs
            for(GenericInterface i :interfaceList){
                if (i instanceof DisplayInterface && i.interfaceStarted()){
                    ((DisplayInterface) i).updateUPS(41.9);
                    ((DisplayInterface) i).updateFPS(125);
                    ((DisplayInterface) i).drawAll();
                }

                if (i instanceof UdpOutputInterface && i.interfaceStarted()){
                    ((UdpOutputInterface) i).send_speed((byte) 100);

                }
            }

        }
    }
}
