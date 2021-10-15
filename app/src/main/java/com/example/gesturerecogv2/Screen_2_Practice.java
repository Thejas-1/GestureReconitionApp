package com.example.gesturerecogv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class Screen_2_Practice extends AppCompatActivity {
    private String pathToVideo = "android.resource://com.example.gesturerecog/";
    private String selectedString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_2__practice);

        Button playVideo = (Button) findViewById(R.id.button2);


        Bundle b = getIntent().getExtras();
        selectedString = b.getString("selectedItem");

        Global g = Global.getInstance();
        g.setCurPos(g.getCounter(g.getGestureNumber(selectedString)-1));

        System.out.println("Selected Value"+ b.getString("selectedItem"));
        switch (b.getString("selectedItem")){
            case "Turn on lights":
                pathToVideo += R.raw.hlighton;
                break;
            case "Turn off lights":
                pathToVideo += R.raw.hlightoff;
                break;
            case "Turn on fan":
                pathToVideo += R.raw.hfanon;
                break;
            case "Turn off fan":
                pathToVideo += R.raw.hfanoff;
                break;
            case "Increase Fan Speed":
                pathToVideo += R.raw.hincreasefanspeed;
                break;
            case "decrease fan speed":
                pathToVideo += R.raw.hdecreasefanspeed;
                break;
            case "Set Thermostat to specified temperature":
                pathToVideo += R.raw.hsetthermo;
                break;
            case "0":
                pathToVideo += R.raw.h0;
                break;
            case "1":
                pathToVideo += R.raw.h1;
                break;
            case "2":
                pathToVideo += R.raw.h2;
                break;
            case "3":
                pathToVideo += R.raw.h3;
                break;
            case "4":
                pathToVideo += R.raw.h4;
                break;
            case "5":
                pathToVideo += R.raw.h5;
                break;
            case "6":
                pathToVideo += R.raw.h6;
                break;
            case "7":
                pathToVideo += R.raw.h7;
                break;
            case "8":
                pathToVideo += R.raw.h8;
                break;
            case "9":
                pathToVideo += R.raw.h9;
                break;

            default:
                break;
        }

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pathToVideo = "android.resource://com.example.gesturerecog/" + R.raw.h0;
                videoView.setVideoURI(Uri.parse(pathToVideo));
                videoView.start();
            }
        });

        Button goBack = (Button) findViewById(R.id.button3);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Screen_2_Practice.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button practice = (Button) findViewById(R.id.button4);
        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("selectedItem",selectedString);
                System.out.println("Sent String"+selectedString);
                Intent newIntent = new Intent(Screen_2_Practice.this,Screen_3_Vid_Record.class);
                newIntent.putExtras(b);
                startActivity(newIntent);
            }
        });

    }
}