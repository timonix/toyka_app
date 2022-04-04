package com.example.toyka_app;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LOGGER {

    private static Map<Class<?>,LOGGER> loggers = new HashMap<>();

    private Level level = Level.INFO;
    private final String myClass;

    private LOGGER(Class<?> aclass) {
        this.myClass = aclass.getSimpleName();
    }

    public static LOGGER getLogger(Class<?> aclass) {
        if (!loggers.containsKey(aclass))
            loggers.put(aclass,new LOGGER(aclass));
        return loggers.get(aclass);

    }


    public void log(Level l, String msg){
        if (level.intValue()<=l.intValue()) {
            print(msg);
        }
    }

    private void print(String msg){
        System.out.println(myClass + " :" + msg);
    }
}
