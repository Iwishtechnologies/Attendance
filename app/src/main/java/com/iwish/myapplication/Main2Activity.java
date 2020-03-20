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

public class Main2Activity extends AppCompatActivity {
    LinearLayout layout;
    private Timer timer;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        layout = findViewById(R.id.layout);
        userSession= new UserSession(this);


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager)Main2Activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true)
                {
                  userSession.setInternet(String.valueOf(true));
                  timer.cancel();
             Intent intent= new Intent(Main2Activity.this,Splash.class);
             startActivity(intent);
             
                }


            }

        }, 0, 1000);


    }


    public void onBack() {
       onBackPressed();
    }
}
