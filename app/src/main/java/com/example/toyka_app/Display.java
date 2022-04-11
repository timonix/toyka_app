package com.example.toyka_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.icu.util.Output;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Display extends SurfaceView implements SurfaceHolder.Callback {
    private final Context context;


    private long startTime = System.nanoTime();

    private double ups = 0;
    private double batteryLevel = 0;

    private String[] cosole = {"","","",""};

    private boolean displayStarted = false;

    SurfaceHolder surfaceHolder = getHolder();
    private boolean should_hide_menu_bar;
    private long smoothTime = System.nanoTime();;

    public final Joystick joy_direction;
    private Integer joy_direction_pointer_id;

    public final Joystick joy_speed;
    private Integer joy_speed_pointer_id;


    public Display(Context context) {
        super(context);

        joy_direction = new Joystick(8.0f/10,7.0f/10,Joystick.HORIZONTAL, context);
        joy_speed = new Joystick(1.0f/9,2.0f/3,Joystick.VERTICAL, context);

        surfaceHolder.addCallback(this);
        this.context = context;
        setFocusable(true);

        new Thread(() -> {
            while(true){
                drawAll();
            }
        }).start();
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
        joy_speed.draw(canvas);
        joy_direction.draw(canvas);
    }


    public boolean interfaceStarted(){
        return displayStarted;
    }

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


    public void updateUPS(double ups){
        this.ups = ups;
    }

    public void setJoystickLocation() {
        //joy_speed.setLocation(touchX, touchY);
        //joy_direction.setLocation(touchX, touchY);
    }

    public void updateDebugConsole(String debug,int line) {

        cosole[line] = debug;
    }


    public Joystick getJoyStickSpeed() {
        return joy_speed;
    }


    public Joystick getJoyStickDirection() {
        return joy_direction;
    }

    private void drawConsole(@NonNull Canvas canvas){

        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(50);

        for(int i = 0; i< cosole.length ; i++){
            canvas.drawText(cosole[i],100,100 + 55*i,paint);

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

        canvas.drawText("FPS:"+averageUPS,100,450,paint);


    }


    private void drawUPS(@NonNull Canvas canvas) {
        String averageFPS = Double.toString(ups);
        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS:"+averageFPS,100,390,paint);
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                //Bind
                int activePointer = event.getPointerId(event.getActionIndex());
                int pointer = event.findPointerIndex(activePointer);
                float touchX = event.getX(pointer);
                float touchY = event.getY(pointer);
                if (joy_speed.isMyJoystick(touchX,touchY))
                    joy_speed_pointer_id = activePointer;
                if (joy_direction.isMyJoystick(touchX,touchY))
                    joy_direction_pointer_id = activePointer;
                break;
            case MotionEvent.ACTION_MOVE:
                for(int i = 0; i<event.getPointerCount();i++){
                    activePointer = event.getPointerId(i);
                    pointer = event.findPointerIndex(activePointer);
                    touchX = event.getX(pointer);
                    touchY = event.getY(pointer);
                    System.out.println("active:"+activePointer+" ,speed:"+joy_speed_pointer_id);
                    if (joy_speed_pointer_id != null && activePointer == joy_speed_pointer_id) {
                        joy_speed.setLocation(touchX,touchY,false);
                    }else if (joy_direction_pointer_id != null && activePointer == joy_direction_pointer_id){
                        joy_direction.setLocation(touchX,touchY,false);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                //unbind
                activePointer = event.getPointerId(event.getActionIndex());
                pointer = event.findPointerIndex(activePointer);
                if (joy_speed_pointer_id != null && activePointer == joy_speed_pointer_id) {
                    joy_speed_pointer_id = null;
                    joy_speed.setLocation(null,null,true);
                }
                if (joy_direction_pointer_id != null && activePointer == joy_direction_pointer_id){
                    joy_direction_pointer_id = null;
                    joy_direction.setLocation(null,null,true);
                }
                break;

        }

        return true;
    }


    public void updateBatteryIndicator(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
