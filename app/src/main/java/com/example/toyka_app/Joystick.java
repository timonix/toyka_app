package com.example.toyka_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Joystick {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private int type;

    private double x;
    private double y;
    private final Context context;

    private double x_centre;
    private double y_centre;

    private double startX;
    private double startY;
    private double endX;
    private double endY;


    public Joystick(int x_centre, int y_centre, int type, Context context) {
        this.x_centre = x_centre;
        this.y_centre = y_centre;

        this.x = x_centre;
        this.y = y_centre;
        this.context = context;

        if (type == HORIZONTAL)
            this.type = HORIZONTAL;
        else
            this.type = VERTICAL;
    }



    public void draw(Canvas canvas){
        int h = canvas.getHeight();

        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context,R.color.cerise);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float)this.x, (float)this.y, 100, paint);
    }

    public void setLocation(double x, double v) {
    }
}
