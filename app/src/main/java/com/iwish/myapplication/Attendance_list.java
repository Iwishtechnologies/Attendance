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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
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

import ir.hamiss.internetcheckconnection.InternetAvailabilityChecker;
import ir.hamiss.internetcheckconnection.InternetConnectivityListener;

public class Attendance_list extends AppCompatActivity implements InternetConnectivityListener {
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    KProgressHUD kProgressHUD;
    RecyclerView ridehistory;
    List<Attendancedata> attendancedata;
    TextView feedback,name,hospital;
    ImageView superin,superlive,superout,superphoto,superright,superrightout;
    UserSession userSession;
   public static final int REQUSET_IMAGE_CAPTURE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        kProgressHUD= new KProgressHUD(Attendance_list.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        userSession= new UserSession(Attendance_list.this);
        ridehistory= findViewById(R.id.recycle);
        feedback= findViewById(R.id.feedback);
        superin= findViewById(R.id.supercamera);
        superlive= findViewById(R.id.superlive);
        superout= findViewById(R.id.superoutcamera);
        name= findViewById(R.id.supername);
        hospital= findViewById(R.id.hospital);
        superphoto= findViewById(R.id.superphoto);
        superright= findViewById(R.id.superright);
        superrightout= findViewById(R.id.superoutright);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Attendance_list.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ridehistory.setLayoutManager(linearLayoutManager);

        name.setText(userSession.getUserDetails().get("supervisorname"));

        Glide.with(Attendance_list.this)
                .load("http://173.212.226.143/hospital/"+userSession.getUserDetails().get("photo"))
                .into(superphoto);

//        superphoto.setImageURI(Uri.parse("http://173.212.226.143/hospital/"+userSession.getUserDetails().get("photo")));

        Log.e("phoyt","http://173.212.226.143/hospital/"+userSession.getUserDetails().get("photo"));

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Attendance_list.this,Feedback.class);
                startActivity(intent);
                Animatoo.animateZoom(Attendance_list.this);
            }
        });


        superin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Attendance_list.this,SuperTakePicture.class);
                cameraIntent.putExtra("in/out","in");
                startActivity(cameraIntent);
                Animatoo.animateZoom(Attendance_list.this);
                superin.setVisibility(View.GONE);
                superright.setVisibility(View.VISIBLE);
                userSession.setinstatus(String.valueOf(true));
            }
        });

        superout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSession.getinstatus()){
                    Intent cameraIntent = new Intent(Attendance_list.this,SuperTakePicture.class);
                    cameraIntent.putExtra("in/out","out");
                    startActivity(cameraIntent);
                    Animatoo.animateZoom(Attendance_list.this);
                    superout.setVisibility(View.GONE);
                    superrightout.setVisibility(View.VISIBLE);

                }
                else
                {
                    Toast.makeText(Attendance_list.this, "In Attendance is Absent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        superlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSession.getinstatus()){
                    Intent cameraIntent = new Intent(Attendance_list.this,SuperTakePicture.class);
                    cameraIntent.putExtra("in/out","Live");
                    startActivity(cameraIntent);
                    Animatoo.animateZoom(Attendance_list.this);
//                    holder.outcamera.setVisibility(View.GONE);
//                    holder.outright.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(Attendance_list.this, "In Attendance is Absent", Toast.LENGTH_SHORT).show();
                }
            }
        });


         setProgressDialog("Fatching list");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.LIST);
        connectionServer.buildParameter("id",userSession.getUserDetails().get("supervisorid"));

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


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected){
            Intent intent= new Intent(Attendance_list.this,Attendance_list.class);

            startActivity(intent);
            Animatoo.animateCard(Attendance_list.this);
        }
        else {
            Intent intent= new Intent(Attendance_list.this,Main2Activity.class);
            intent.putExtra("activity","attendance");
            startActivity(intent);
            Animatoo.animateCard(Attendance_list.this);
        }
    }

}
