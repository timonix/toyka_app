package com.example.toyka_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Toyka_Display disp = new Toyka_Display(this);
        setContentView(disp);
        MainLoop main = new MainLoop();
        main.addInterface(disp);

        Toyka_UDP udp_io = new Toyka_UDP();
        main.addInterface(udp_io);

        main.start();


    }
}