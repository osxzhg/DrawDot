package com.example.drawdot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.security.KeyStore;
import java.util.*;

import java.util.ArrayList;

public class DrawView extends View implements View.OnTouchListener, View.OnLongClickListener {
    ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    Random random=new Random();
    float radius;
    boolean israndom = false;
    boolean isActDown = false;
    boolean isActMove = false;
    boolean isActUp = false;
    boolean isLongPress=true;
    int color=10;

    public DrawView(Context context) {
        super(context);
        setOnTouchListener(this);
        setOnLongClickListener(this);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        setOnLongClickListener(this);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF pointF = new PointF();
        pointF.set(event.getX(),event.getY());


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isActDown = true;
                isActMove = false;
                isActUp = false;
                points.add(new MyPoint(pointF,radius));
                break;
            case MotionEvent.ACTION_MOVE:
                isActMove = true;
                points.add(new MyPoint(pointF,radius));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isActUp = true;
                if(isLongPress){
                    points.remove(points.size()-1);
                }
                else if(!isActMove){
                    invalidate();
                }
                break;
            default:
                return false;
        }

        return true;
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        for(MyPoint point: points){
            if(israndom){
                paint.setColor(random.nextInt());
            }
            canvas.drawCircle(point.pt.x, point.pt.y,point.radius+5, paint);
        }


    }

    @Override
    public boolean onLongClick(View v) {
        //radius=50;
        //invalidate();
        return true;
    }
}
