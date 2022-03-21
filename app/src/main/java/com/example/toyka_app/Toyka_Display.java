package com.example.toyka_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Toyka_Display extends SurfaceView implements SurfaceHolder.Callback, DisplayInterface {
    private final Context context;

    private Joystick joy_direction = new Joystick(40,50,Joystick.HORIZONTAL);
    private Joystick joy_speed = new Joystick(40,50,Joystick.VERTICAL);

    private long startTime = System.nanoTime();
    private double ups = 0;
    private String[] cosole = {"","","",""};

    private boolean displayStarted = false;

    SurfaceHolder surfaceHolder = getHolder();
    private boolean should_hide_menu_bar;
    private long smoothTime = System.nanoTime();;

    public Toyka_Display(Context context) {
        super(context);
        surfaceHolder.addCallback(this);
        this.context = context;
        setFocusable(true);
    }

    @Override
    public void draw(Canvas canvas) {
        long elapsed = System.nanoTime() - startTime;
        startTime = System.nanoTime();
        smoothTime = (smoothTime*9+elapsed)/10;
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas,smoothTime);
        drawConsole(canvas);
    }

    @Override
    public boolean interfaceStarted(){
        return displayStarted;
    }

    @Override
    public void drawAll() {

        Canvas c = surfaceHolder.lockCanvas();

        if (c == null) {
            should_hide_menu_bar = true;
            return;
        }
        if (should_hide_menu_bar){
            should_hide_menu_bar = false;

            ((MainActivity)context).getSupportActionBar().hide();
        }
        draw(c);
        surfaceHolder.unlockCanvasAndPost(c);
    }

    @Override
    public void updateUPS(double ups){
        this.ups = ups;
    }

    @Override
    public void setJoystickLocation(double x, double y) {
        joy_direction.setLocation(x,0.0);
        joy_speed.setLocation(0.0,y);
    }

    @Override
    public void updateDebugConsole(String debug,int line) {

        cosole[line] = debug;
    }

    private void drawConsole(@NonNull Canvas canvas){
        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(50);

        for(int i = 0; i< cosole.length ; i++){
            canvas.drawText(cosole[i],100,100 + 20*i,paint);

        }
    }

    private void drawJoystick(@NonNull Canvas canvas){

    }

    private void drawFPS(@NonNull Canvas canvas, long elapsed){
        String averageUPS = Double.toString(1.0/(elapsed*Math.pow(10,-9)));
        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(50);

        canvas.drawText("UPS:"+averageUPS,100,500,paint);


    }


    private void drawUPS(@NonNull Canvas canvas) {
        String averageFPS = Double.toString(ups);
        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS:"+averageFPS,100,280,paint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        displayStarted = true;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
