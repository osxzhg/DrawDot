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
import android.widget.Button;
import android.widget.SeekBar;

import java.security.KeyStore;
import java.util.*;

import java.util.ArrayList;

public class DrawView extends View implements View.OnTouchListener, View.OnLongClickListener {
    ArrayList<MyPoint> points = new ArrayList<>();
    Random random=new Random();
    float radius;
    final LinkedList<Long> myTime = new LinkedList<>();
    boolean israndom = false;
    boolean isSingleColor = true;
    boolean isCircleColor = false;
    boolean isActDown = false;
    boolean isActMove = false;
    boolean isActUp = false;
    boolean isLongPress=false;
    int color=10;
    int[] colorList ={Color.BLACK, Color.BLUE ,Color.CYAN, Color.DKGRAY,Color.GRAY,Color.GREEN,
            Color.LTGRAY,Color.MAGENTA,Color.RED,Color.YELLOW};

    int colorIndext = 0;
    int currentStep = 0;
    int maxStep = 0;
    float xActDown=0; // the x of action down
    float yActDown=0; // the y of action down

    Thread consumer = new Thread("Consumer") // This example only includes one consumer but there could be more
    {
        int ntime;
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
                        //Log.d("myd","ok");
                        if( (System.currentTimeMillis()-myTime.getFirst()) > 1000 ){
                            Log.d("myd","ok");
                            isLongPress=true;
                            ntime=(int) (System.currentTimeMillis()-myTime.getFirst())/1000 % colorList.length;
                            // not move from on touch , change color every second
                            if(ntime!=colorIndext && !isActMove){
                                colorIndext = ntime;
                                //isLongPress=true;
                                Log.d("ntime",Integer.toString(colorIndext));
                                invalidate();
                            }

                        }
                        /*
                        else{
                            Log.d("myd","less than 100");
                        }*/



                    }
                }


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
    int count=0;
    int fingers;
    @Override
    public boolean onTouch(View v, MotionEvent event) {


        //Log.d("fingers","ontouch"+Integer.toString(++count));



        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("fingers","down"+Integer.toString(count)+" x:" + event.getX()+", "+"y:"+event.getY());
                isActDown = true;
                isActMove = false;
                isActUp = false;

                xActDown=event.getX();
                yActDown=event.getY();
                int len = points.size();

                for(int i = 0; i < points.size(); i++) {
                    if (points.get(i).getStepnumber()> currentStep) {

                        points.remove(i);
                        --len;
                        --i;

                        len = points.size();
                    }
                }
                currentStep++;

                if(isCircleColor){
                    colorIndext = (colorIndext +1 )% colorList.length;
                }
                if(isSingleColor) {
                    synchronized (myTime) {
                        myTime.add(System.currentTimeMillis());
                        myTime.notify();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                fingers=event.getPointerCount();
                float distance=0;
                if(isSingleColor) {
                    if (fingers == 1) {
                        distance = (event.getX() - xActDown) * (event.getX() - xActDown) + (event.getX() - xActDown) * (event.getX() - xActDown);
                        Log.d("distance", Float.toString(distance));
                        if (distance > 200) {
                            isActMove = true;
                            points.add(new MyPoint(event.getX(), event.getY(), radius,colorList[colorIndext] , currentStep));
                        }
                    }else {
                        for(int i=0;i<fingers;i++){
                            Log.d("fingers","move"+Integer.toString(count)+ " x:" + event.getX(i) + ", " + "y:" + event.getY(i));
                            points.add(new MyPoint(event.getX(i),event.getY(i), radius, colorList[colorIndext], currentStep));
                        }
                    }
                }else {
                    isActMove=true;
                }
                //Log.d("myd","distance:"+Float.toString(distance)+","+Boolean.toString(isActMove));
                //Log.d("fingers","move"+Integer.toString(fingers));
/*
                for(int i=0;i<fingers;i++){
                    Log.d("fingers","move"+Integer.toString(count)+ " x:" + event.getX(i) + ", " + "y:" + event.getY(i));
                    points.add(new MyPoint(event.getX(i),event.getY(i),radius,random.nextInt(),currentStep));
                } */

                if(israndom){
                    for(int i=0;i<fingers;i++){
                        Log.d("fingers","move"+Integer.toString(count)+ " x:" + event.getX(i) + ", " + "y:" + event.getY(i));
                        points.add(new MyPoint(event.getX(i),event.getY(i), radius, random.nextInt(), currentStep));
                    }
                } else if(isCircleColor) {
                    for(int i=0;i<fingers;i++){
                        Log.d("fingers","move"+Integer.toString(count)+ " x:" + event.getX(i) + ", " + "y:" + event.getY(i));
                        points.add(new MyPoint(event.getX(i),event.getY(i), radius, colorList[colorIndext], currentStep));
                    }
                }
/*                synchronized(myTime) {
                    myTime.removeFirst();
                }*/
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                fingers=event.getPointerCount();
                Log.d("fingers","UP"+Integer.toString(count)+" x:" + event.getX()+", "+"y:"+event.getY());
                //Log.d("fingers","up"+Integer.toString(fingers));
                isActUp = true;
                isActDown = false;
                isLongPress =false;
                if( isSingleColor) {
                    synchronized (myTime) {
                        myTime.removeFirst();
                    }
                }
                //Log.d("myd",Integer.toString(currentStep));
               // Log.d("myd",Boolean.toString(isLongPress));
                //Log.d("myd", String.valueOf(isLongPress));
                if(isLongPress && !isActMove){
                    //points.remove(points.size()-1);
                    currentStep--;
                }
                else if(!isActMove){
                //    Log.d("myd", String.valueOf(isActMove));

                        Log.d("fingers","up"+Integer.toString(count)+ " x:" + event.getX() + ", " + "y:" + event.getY());
                        points.add(new MyPoint(event.getX(),event.getY(), radius, colorList[colorIndext], currentStep));
                    invalidate();
                }
                maxStep=currentStep;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                fingers=event.getPointerCount();
                for(int i=0;i<fingers;i++){
                    //pointF.set(event.getX(i),event.getY(i));
                    //points.add(new MyPoint(pointF, radius, colorList[colorIndext], currentStep));
                    Log.d("fingers","pdown"+Integer.toString(count)+" x:" + event.getX(i)+", "+"y:"+event.getY(i));
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                fingers=event.getPointerCount();
                for(int i=0;i<fingers;i++){
                    //pointF.set(event.getX(i),event.getY(i));
                    //points.add(new MyPoint(pointF, radius, colorList[colorIndext], currentStep));
                    Log.d("fingers","pup"+Integer.toString(count)+" x:" + event.getX(i)+", "+"y:"+event.getY(i));
                    if(israndom){
                        color=random.nextInt();
                    }else {
                        color=colorList[colorIndext];
                    }

                    points.add(new MyPoint(event.getX(i),event.getY(i), radius, color, currentStep));
                }
                break;

            default:
                fingers=event.getPointerCount();
                //Log.d("fingers","default"+Integer.toString(fingers));
                for(int i=0;i<fingers;i++){
                    //pointF.set(event.getX(i),event.getY(i));
                    //points.add(new MyPoint(pointF, radius, colorList[colorIndext], currentStep));
                    Log.d("fingers","default"+Integer.toString(count)+" x:" + event.getX(i)+", "+"y:"+event.getY(i));
                }
                invalidate();
                return false;
        }

        return true;
    }

    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        for(MyPoint point: points){
            Log.d("fingers",Float.toString(point.x)+Float.toString(point.y));
            Log.d("points","number");
            paint.setColor(point.color);
            /*if(isLongPress && !isActMove){
                paint.setColor(colorList[colorIndext]);
            }*/
            if(point.getStepnumber()<=currentStep){
                canvas.drawCircle(point.x, point.y,point.radius+5, paint);
            }

        }
            if(isLongPress && !isActMove){
                paint.setColor(colorList[colorIndext]);
                canvas.drawCircle(10, 10,50, paint);
            }


    }

    @Override
    public boolean onLongClick(View v) {
        //radius=50;
        //invalidate();
        return true;
    }




}
