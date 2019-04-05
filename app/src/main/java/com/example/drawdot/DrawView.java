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
    ArrayList<MyPoint> points = new ArrayList<>();

    Random random=new Random();
    float radius;
    final LinkedList<Long> myTime = new LinkedList<>();
    boolean israndom = false;
    boolean isSingleColor = false;
    boolean isCircleColor = false;
    boolean isActDown = false;
    boolean isActMove = false;
    boolean isActUp = false;
    boolean isLongPress=false;
    int color=10;
    int[] colorList ={Color.BLACK, Color.BLUE ,Color.CYAN, Color.DKGRAY,Color.GRAY,Color.GREEN,
            Color.LTGRAY,Color.MAGENTA,Color.RED,Color.TRANSPARENT, Color.WHITE,Color.YELLOW};
    int colorIndext = 0;

    Thread consumer = new Thread("Consumer") // This example only includes one consumer but there could be more
    {
        public void run()
        {
            while(true) // yes run forever
            {
                synchronized(myTime)
                {
                    if(myTime.isEmpty())
                    {
                        try
                        {
                            Log.d("myd","wait");
                            myTime.wait(); // signal that the list is empty and that this thread needs to block
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        Log.d("myd","isAwake");
                    }
                    else
                    {
                        if( (System.currentTimeMillis()-myTime.getFirst()) > 2000 ){
                            Log.d("myd","ok");
                            isLongPress=true;
                        }
                        else{
                            Log.d("myd","less than 100");
                        }

                    }
                }
             try{
                Thread.currentThread().sleep(1000);
             }catch(InterruptedException ie){
                 ie.printStackTrace(); }

            }


        }
    };

    public DrawView(Context context) {
        super(context);
        setOnTouchListener(this);
        setOnLongClickListener(this);
        consumer.start();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        setOnLongClickListener(this);
        consumer.start();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        setOnLongClickListener(this);
        consumer.start();
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
                if(israndom){
                    points.add(new MyPoint(pointF, radius, random.nextInt()));
                } else if(isCircleColor){
                    colorIndext = (colorIndext +1 )% colorList.length;
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext]));
                } else {
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext]));
                }
                synchronized(myTime) {
                    myTime.add(System.currentTimeMillis());
                    myTime.notify();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isActMove = true;
                if(israndom){
                    points.add(new MyPoint(pointF, radius, random.nextInt()));
                } else {
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext]));
                }
/*                synchronized(myTime) {
                    myTime.removeFirst();
                }*/
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isActUp = true;
                isActDown = false;
                synchronized(myTime) {
                    myTime.removeFirst();
                }
                Log.d("myd", String.valueOf(isLongPress));
                if(isLongPress){
                    points.remove(points.size()-1);
                    isLongPress=false;
                }
                else if(!isActMove){
                    Log.d("myd", String.valueOf(isActMove));
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
            paint.setColor(point.color);
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
