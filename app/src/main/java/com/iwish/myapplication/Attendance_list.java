package com.iwish.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.iwish.myapplication.Adapter.AttendanceAdapter;
import com.iwish.myapplication.Adapter.Attendancedata;
import com.iwish.myapplication.config.Constants;
import com.iwish.myapplication.connection.ConnectionServer;
import com.iwish.myapplication.connection.JsonHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mindorks.paracamera.Camera;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Attendance_list extends AppCompatActivity {
    KProgressHUD kProgressHUD;
    RecyclerView ridehistory;
    List<Attendancedata> attendancedata;
    TextView feedback;
   public static final int REQUSET_IMAGE_CAPTURE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        kProgressHUD= new KProgressHUD(Attendance_list.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        ridehistory= findViewById(R.id.recycle);
        feedback= findViewById(R.id.feedback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Attendance_list.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ridehistory.setLayoutManager(linearLayoutManager);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Attendance_list.this,Feedback.class);
                startActivity(intent);
                Animatoo.animateZoom(Attendance_list.this);
            }
        });





         setProgressDialog("Fatching list");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.LIST);
        connectionServer.buildParameter("id","1");

        attendancedata = new ArrayList<Attendancedata>();

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");

                        Log.e("json", String.valueOf(jsonArray));

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            jsonHelper.setChildjsonObj(jsonArray,i);

                            attendancedata.add(new Attendancedata(jsonHelper.GetResult("worker_id"),jsonHelper.GetResult("workerName"),jsonHelper.GetResult("photo"),false));

                        }

                        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(Attendance_list.this,attendancedata);

                        ridehistory.setAdapter(attendanceAdapter);

                    }
                    else
                    {
                        remove_progress_Dialog();
                    }
                }

            }
        });
    }


    public void setProgressDialog(String msg) {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    public void remove_progress_Dialog() {
        kProgressHUD.dismiss();
    }


    public  void  Takepicture(View view){
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
    }

}
