package com.example.toyka_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    public final Joystick joy_speed;


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

        Float touchX0 = null;
        Float touchX1 = null;
        Float touchY0 = null;
        Float touchY1 = null;
        Integer mActivePointer1 = null;
        Integer pointer1 = null;

        // Get the pointer ID
        int mActivePointer0 = event.getPointerId(0);
        if (event.getPointerCount() > 1){
            mActivePointer1 = event.getPointerId(1);
            pointer1 = event.findPointerIndex(mActivePointer1);
        }


        // Use the pointer ID to find the index of the active pointer
        // and fetch its position
        int pointer0 = event.findPointerIndex(mActivePointer0);

        if (pointer0 == 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    touchX0 = event.getX(pointer0);
                    touchY0 = event.getY(pointer0);
                    if (pointer1 != null){
                        touchX1 = event.getX(pointer1);
                        touchY1 = event.getY(pointer1);
                    }
                    //System.out.println(pointerIndex);
                    break;
                case MotionEvent.ACTION_UP:
                    touchX0 = null;
                    touchY0 = null;
                    if (pointer1 != null){
                        touchX1 = null;
                        touchY1 = null;
                    }
                    break;
            }
        }
        /*
        if (pointer1 == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    touchX1 = event.getX(pointer1);
                    touchY1 = event.getY(pointer1);
                    //System.out.println(pointerIndex);
                    break;
                case MotionEvent.ACTION_UP:
                    touchX1 = null;
                    touchY1 = null;
                    break;
            }


        }
 */

        joy_speed.setLocation(touchX0, touchY0);
        joy_direction.setLocation(touchX0, touchY0);
        if (event.getPointerCount() > 1) {
            joy_speed.setLocation(touchX1, touchY1);
            joy_direction.setLocation(touchX1, touchY1);
        }
        //System.out.println(touchX);

        return true;
    }


    public void updateBatteryIndicator(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
