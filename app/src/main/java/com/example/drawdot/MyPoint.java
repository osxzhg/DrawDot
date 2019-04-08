package com.example.drawdot;


class MyPoint {
    float x; //coordinate of x and y
    float y;
    float radius; //radius of the circle
    private int color;
    private int stepnumber;

    int getColor() {
        return this.color ;
    }
    int getStepnumber(){
        return this.stepnumber;
    }

    MyPoint(float x, float y, float radius, int color,int stepnumber){
        this.x=x;
        this.y=y;
        this.radius=radius;
        this.color=color;
        this.stepnumber=stepnumber;
    }
}
