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

import java.time.LocalDateTime;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawView drawView = findViewById(R.id.view2);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar3);
        final LinkedList<String> words = new LinkedList<String>();
        final LinkedList<Long> myTime = new LinkedList<Long>();

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

        Button btn = (Button) findViewById(R.id.button_redo);
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
                    synchronized(words)
                    {
                        if(words.isEmpty())
                        {
                            try
                            {
                                Log.d("myd","wait");
                                words.wait(); // signal that the list is empty and that this thread needs to block
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            Log.d("myd","isAwake");
                        }
                        else
                        {
                            Log.d("myd",words.removeFirst());
                        }
                    }
                }


            }
        };
        long ctime=System.currentTimeMillis();
        myTime.add(ctime);
        Log.d("myd",Long.toString(myTime.removeFirst()));
        consumer.start();
        int i;
        String inputData;
        for(i=0;i<10;i++){
           inputData="hello"+Integer.toString(i);
            synchronized(words)
            {
                words.add(inputData);
                words.notify(); // signal that we have added some data to our list
            }

        }



    }
}
