package com.example.toyka_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Joystick {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private int type;
    private final float horizontalPosition = 1.0f/9;
    private final float verticalPosition = 2.0f/3;

    private double x;
    private double y;
    private final Context context;

    private float x_centre;
    private float y_centre;

    private float length;

    private float startX;
    private float startY;
    private float endX;
    private float endY;


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

        // Calculating the joysticks centre and length
        float h = canvas.getHeight();
        float w = canvas.getWidth();

        this.y_centre = verticalPosition * h;
        this.x_centre = horizontalPosition * w;

        if (this.type == HORIZONTAL)
            this.length = h/3;
        else
            this.length = w/5;

        // Drawing the joystick knob
        Paint paint = new Paint();
        int color =  ContextCompat.getColor(context, R.color.cerise);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle((float)this.x, (float)this.y, 100, paint);

        // Drawing the joystick path
        color = ContextCompat.getColor(context, android.R.color.darker_gray);
        paint.setColor(color);

        if (this.type == HORIZONTAL)
            canvas.drawLine(this.x_centre-this.length/2, this.y_centre, this.x_centre+this.length/2, this.y_centre, paint);
        else
            canvas.drawLine(this.x_centre, this.y_centre-this.length/2, this.x_centre, this.y_centre+this.length/2, paint);
    }

    public void setLocation(double x, double v) {
    }
}
