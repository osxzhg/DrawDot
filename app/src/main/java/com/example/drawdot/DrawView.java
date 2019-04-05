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
            Color.LTGRAY,Color.MAGENTA,Color.RED,Color.TRANSPARENT, Color.WHITE,Color.YELLOW};

    int colorIndext = 0;
    int currentStep = 0;
    int maxStep = 0;

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
                            //isLongPress=true;
                            ntime=(int) (System.currentTimeMillis()-myTime.getFirst())/1000 % colorList.length;
                            // isActMove need to get rid of slightly move
                            if(ntime!=colorIndext && !isActMove){
                                colorIndext = ntime;
                                isLongPress=true;
                                Log.d("myd",Integer.toString(ntime));
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF pointF = new PointF();
        pointF.set(event.getX(),event.getY());
        pointF.length();
        int fingers=event.getPointerCount();




        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("fingers","down"+Integer.toString(fingers));
                isActDown = true;
                isActMove = false;
                isActUp = false;
                currentStep++;
                if(israndom){
                    points.add(new MyPoint(pointF, radius, random.nextInt(),currentStep));
                } else if(isCircleColor){
                    colorIndext = (colorIndext +1 )% colorList.length;
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext],currentStep));
                } else {
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext],currentStep));
                }
                if(isSingleColor) {
                    synchronized (myTime) {
                        myTime.add(System.currentTimeMillis());
                        myTime.notify();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isActMove = true;
                Log.d("fingers","move"+Integer.toString(fingers));
                if(israndom){
                    for(int i=0;i<fingers;i++){
                        pointF.set(event.getX(i),event.getY(i));
                        points.add(new MyPoint(pointF, radius, random.nextInt(), currentStep));
                    }


                } else {
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext],currentStep));
                }
/*                synchronized(myTime) {
                    myTime.removeFirst();
                }*/
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("fingers","up"+Integer.toString(fingers));
                isActUp = true;
                isActDown = false;
                if( isSingleColor) {
                    synchronized (myTime) {
                        myTime.removeFirst();
                    }
                }
                Log.d("myd",Integer.toString(currentStep));
                Log.d("myd",Boolean.toString(isLongPress));
                //Log.d("myd", String.valueOf(isLongPress));
                if(isLongPress){
                    points.remove(points.size()-1);
                    currentStep--;
                    invalidate();
                    isLongPress=false;
                }
                else if(!isActMove){
                //    Log.d("myd", String.valueOf(isActMove));
                    invalidate();
                }
                maxStep=currentStep;
                break;
            default:
                Log.d("fingers","default"+Integer.toString(fingers));
                for(int i=0;i<fingers;i++){
                    pointF.set(event.getX(i),event.getY(i));
                    points.add(new MyPoint(pointF, radius, colorList[colorIndext], currentStep));
                }
                invalidate();
                return false;
        }

        return true;
    }

    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        for(MyPoint point: points){
            Log.d("points","number");
            paint.setColor(point.color);
            if(isLongPress && !isActMove){
                paint.setColor(colorList[colorIndext]);
            }
            if(point.getStepnumber()<=currentStep){
                canvas.drawCircle(point.pt.x, point.pt.y,point.radius+5, paint);
            }

        }


    }

    @Override
    public boolean onLongClick(View v) {
        //radius=50;
        //invalidate();
        return true;
    }




}
