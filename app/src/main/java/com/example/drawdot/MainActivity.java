package com.example.drawdot;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawView drawView = findViewById(R.id.view2);
        final SeekBar seekBar = findViewById(R.id.seekBar3);
        final LinkedList<String> words = new LinkedList<>();
        final LinkedList<Long> myTime = new LinkedList<>();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                drawView.radius = seekBar.getProgress();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btn =findViewById(R.id.button_redo);
        //int[] colors = {Color.parseColor("#008000"),Color.parseColor("#ADFF2F")};
        int[] colors = {Color.parseColor("#CC0000"),Color.parseColor("#FF0000")};
        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        gd.setCornerRadius(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackground(gd);
        }

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
                            Log.d("myd","bf");
                            if( (System.currentTimeMillis()-myTime.getFirst()) > 5000 ){
                                Log.d("myd","ok");
                            }

                        }
                    }
                }


            }
        };
        long ctime=System.currentTimeMillis();

        synchronized(myTime) {
            myTime.add(ctime);
            myTime.notify();
        }

        consumer.start();





    }
}
