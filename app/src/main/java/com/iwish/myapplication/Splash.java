package com.iwish.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    UserSession userSession;
    CheckConnection checkConnection;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkConnection =new CheckConnection(this);
        userSession= new UserSession(this);
        userSession.setInternet(String.valueOf(false));
//        Intent intent = new Intent(Splash.this,CheckConnection.class);
//        checkConnection.onStartCommand(intent,1,1);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                startActivity(new Intent(Splash.this,MainActivity.class));
                Animatoo.animateFade(Splash.this);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}