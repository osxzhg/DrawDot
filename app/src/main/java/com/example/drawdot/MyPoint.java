package com.example.drawdot;

import android.graphics.PointF;

public class MyPoint {
    PointF pt;
    float radius;
    int color;
    int StepNum;
    int id;
    public MyPoint(PointF pt,float radius,int color){
        this.pt=pt;
        this.radius=radius;
        this.color=color;
    }
}
