package com.example.drawdot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.*;

import java.util.ArrayList;

public class DrawView extends View implements View.OnTouchListener, View.OnLongClickListener {
    ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    Random random=new Random();
    float radius;

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
        points.add(new MyPoint(event.getX(),event.getY()));
        invalidate();
        return false;
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        for(MyPoint pt: points){
            paint.setColor(random.nextInt());
            canvas.drawCircle(pt.x, pt.y,radius+5, paint);
        }


    }

    @Override
    public boolean onLongClick(View v) {
        radius=50;
        //invalidate();
        return true;
    }
}
