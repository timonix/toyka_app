package com.example.toyka_app;

import static java.lang.Math.abs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Joystick {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private int type;
    private float horizontalPosition;   // Relative position of the centre of the joystick from the left. Value between 0-1
    private float verticalPosition;     // Relative position of the centre of the joystick from the top. Value between 0-1

    public double x;
    public double y;
    private final Context context;

    private float x_centre;
    private float y_centre;

    private float length;

    private float radius;

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    private float h;
    private float w;


    public Joystick(float horizontalPosition, float verticalPosition, int type, Context context) {
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;

        this.context = context;

        if (type == HORIZONTAL)
            this.type = HORIZONTAL;
        else
            this.type = VERTICAL;
    }



    public void draw(Canvas canvas){

        // Calculating the joysticks centre, radius and length
        h = canvas.getHeight();
        w = canvas.getWidth();

        this.y_centre = this.verticalPosition * h;
        this.x_centre = this.horizontalPosition * w;

        if (this.x == 0 && this.y == 0) {
            this.x = this.x_centre;
            this.y = this.y_centre;
        }




        // Debug stuff, remove this later plox
        //this.x = this.x_centre;
        //this.y = this.y_centre;
        // ------------------

        if (this.type == HORIZONTAL) {
            this.length = w/4;
            this.startX = this.x_centre - this.length/2;
            this.endX = this.x_centre + this.length/2;
            this.startY = this.y_centre;
            this.endY = this.y_centre;
        }
        else {
            this.length = h/3;
            this.startX = this.x_centre;
            this.endX = this.x_centre;
            this.startY = this.y_centre - this.length/2;
            this.endY = this.y_centre + this.length/2;
        }


        this.radius = (float)Math.sqrt(w*w + h*h)/20;

        // Drawing the joystick path
        int color = ContextCompat.getColor(context, android.R.color.darker_gray);
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(color);

        if (this.type == HORIZONTAL)
            canvas.drawLine(this.x_centre-this.length/2, this.y_centre, this.x_centre+this.length/2, this.y_centre, paint);
        else
            canvas.drawLine(this.x_centre, this.y_centre-this.length/2, this.x_centre, this.y_centre+this.length/2, paint);

        // Drawing the joystick knob
        color =  ContextCompat.getColor(context, R.color.cerise);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle((float)this.x, (float)this.y, this.radius, paint);


    }

    public float calculateSignal() {
        /*
        if (this.type == HORIZONTAL) {

        }
        else {

        }

         */
        return 1;
    }

    public void setLocation(Float touchX, Float touchY) {

        if (touchX != null && touchY != null) {
            if (abs(touchX - this.x) <= this.radius
                    && abs(touchY - this.y) <= this.radius) {
                if (this.x_centre < this.w/2) {
                    if (this.x < this.w/2) {
                        this.x = touchX;
                        this.y = touchY;
                    }
                }
                else {
                    if (this.x > this.w/2) {
                        this.x = touchX;
                        this.y = touchY;
                    }
                }
            }

            if (this.type == HORIZONTAL) {
                this.y = this.y_centre;
                if (this.x < this.startX)
                    this.x = this.startX;
                else if (this.x > this.endX)
                    this.x = this.endX;
            }
            else if (this.type == VERTICAL) {
                this.x = this.x_centre;
                if (this.y < startY)
                    this.y = this.startY;
                if (this.y > endY)
                    this.y = endY;
            }
        }
        else {
            this.x = this.x_centre;
            this.y = this.y_centre;
        }


    }
}
