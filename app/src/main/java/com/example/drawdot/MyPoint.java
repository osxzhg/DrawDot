package com.example.drawdot;

import android.graphics.PointF;

public class MyPoint {
    PointF pt;
    float radius;
    int color;
    private int stepnumber;
    int id;

    public void setColor(int color) {
        this.color = color;
    }
    public int getStepnumber(){
        return this.stepnumber;
    }

    public MyPoint(PointF pt, float radius, int color,int stepnumber){
        this.pt=pt;
        this.radius=radius;
        this.color=color;
        this.stepnumber=stepnumber;
    }
}
