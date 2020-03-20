package com.iwish.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Timer;
import java.util.TimerTask;

class CheckConnection  extends Service {

    private Context mContext;
    Timer timer;
    UserSession userSession;
    Main2Activity main2Activity;

    public CheckConnection(Context context){
        this.mContext = context;

    }

    public boolean isConnectingToInternet(){

        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true)
        {
            return true;
        }

        return false;

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        userSession= new UserSession(mContext);
        main2Activity = new Main2Activity();
         timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true)
                {

                    if(!userSession.getInternet())
                    {
//                        main2Activity.onBackPressed();
                        userSession.setInternet(String.valueOf(true));
                    }
                }
                else
                {
                    if(userSession.getInternet())
                    {
                        userSession.setInternet(String.valueOf(false));
                        Intent intent= new Intent(mContext,Main2Activity.class);
                        mContext.startActivity(intent);
                        Animatoo.animateCard(mContext);
                    }
                }

            }

        }, 0, 1000);




        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
