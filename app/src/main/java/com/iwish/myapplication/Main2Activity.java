package com.iwish.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Timer;
import java.util.TimerTask;

import ir.hamiss.internetcheckconnection.InternetAvailabilityChecker;
import ir.hamiss.internetcheckconnection.InternetConnectivityListener;

public class Main2Activity extends AppCompatActivity  implements InternetConnectivityListener {
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    LinearLayout layout;
    private Timer timer;
    UserSession userSession;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
      Intent intent= getIntent();
      activity= intent.getStringExtra("activity");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        layout = findViewById(R.id.layout);
        userSession = new UserSession(this);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected){
            switch (activity)
            {
                case "login":
                            Intent intent1= new Intent(Main2Activity.this,MainActivity.class);
                            startActivity(intent1);
                            Animatoo.animateCard(Main2Activity.this);
                break;

                case "attendance":
                    Intent intent= new Intent(Main2Activity.this,Attendance_list.class);
                    startActivity(intent);
                    Animatoo.animateCard(Main2Activity.this);
                    break;

                case "feedback":

                    Intent intent2= new Intent(Main2Activity.this,Feedback.class);
                    startActivity(intent2);
                    Animatoo.animateCard(Main2Activity.this);
                    break;



            }


        }
        else {
            Intent intent= new Intent(Main2Activity.this,Main2Activity.class);
            startActivity(intent);
            Animatoo.animateCard(Main2Activity.this);
        }
    }



}
