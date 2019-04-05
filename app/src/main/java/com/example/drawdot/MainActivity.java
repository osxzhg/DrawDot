package com.example.drawdot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
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
        final Button statusButton = findViewById(R.id.button_status);
        final LinkedList<String> words = new LinkedList<>();

        statusButton.setBackgroundColor(Color.GREEN);
        int[] colors = {drawView.colorList[drawView.colorIndext],drawView.colorList[drawView.colorIndext]};
        //create a new gradient color

        //ColorDrawable colorDrawable = new ColorDrawable(drawView.colorList[drawView.colorIndext]);
        //statusButton.setBackground(colorDrawable);
        //statusButton.setBackgroundColor(Color.GREEN);
        /*
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        gd.setCornerRadius(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            statusButton.setBackground(gd);
        }*/

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
        statusButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawView.isSingleColor==true){
                    drawView.isSingleColor=false;
                    drawView.isCircleColor=true;
                    drawView.israndom=false;
                    statusButton.setBackgroundColor(Color.RED);
                } else if(drawView.isCircleColor==true) {
                    drawView.isSingleColor=false;
                    drawView.isCircleColor=false;
                    drawView.israndom=true;
                    statusButton.setBackgroundColor(Color.YELLOW);
                } else {
                    drawView.isSingleColor=true;
                    drawView.isCircleColor=false;
                    drawView.israndom=false;
                    statusButton.setBackgroundColor(Color.GREEN);
                }
            }
        });
/*
        Button btn =findViewById(R.id.button_redo);
        //int[] colors = {Color.parseColor("#008000"),Color.parseColor("#ADFF2F")};
        int[] colors = {Color.parseColor("#CC0000"),Color.parseColor("#FF0000")};
        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        gd.setCornerRadius(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackground(gd);
        }
*/





    }
}
