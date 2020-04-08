package com.iwish.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.iwish.myapplication.Adapter.Attendancedata;
import com.iwish.myapplication.config.Constants;
import com.iwish.myapplication.connection.ConnectionServer;
import com.iwish.myapplication.connection.JsonHelper;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

import ir.hamiss.internetcheckconnection.InternetAvailabilityChecker;
import ir.hamiss.internetcheckconnection.InternetConnectivityListener;

import static com.iwish.myapplication.Attendance_list.REQUSET_IMAGE_CAPTURE;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    KProgressHUD kProgressHUD;
    EditText user,pass;
    Button login;
    public static final int REQUSET_IMAGE_CAPTURE = 101;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};
     UserSession userSession;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSession= new UserSession(MainActivity.this);
        userSession.checkLogin();
        kProgressHUD= new KProgressHUD(MainActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        userSession = new UserSession(MainActivity.this);
        user= findViewById(R.id.user);
        pass= findViewById(R.id.pass);
        login= findViewById(R.id.login);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, REQUSET_IMAGE_CAPTURE);

             if(validate(user.getText().toString(),pass.getText().toString()))
             {
              Login(user.getText().toString(),pass.getText().toString());
             }
            }
        });



    }


    public void Login(String mobile,String pass){
        setProgressDialog("Varifying ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.LOGIN);
        connectionServer.buildParameter("user",mobile);
        connectionServer.buildParameter("pass",pass);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
//                        Toast.makeText(Login.this,"your otp is :"+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");

                        Log.e("json", String.valueOf(jsonArray));

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            jsonHelper.setChildjsonObj(jsonArray,i);

                            userSession.createLoginSession(jsonHelper.GetResult("supervisor_id"),jsonHelper.GetResult("supervisorName"),jsonHelper.GetResult("hospital_id"),jsonHelper.GetResult("image"),"daf");

                        }
                        Intent intent= new Intent(MainActivity.this,Attendance_list.class);
                        startActivity(intent);
                        Animatoo.animateSplit(MainActivity.this);
                    }
                    else
                    {
                        remove_progress_Dialog();
                        Toast.makeText(MainActivity.this,""+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();

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


    public Boolean validate(String usern,String passn)
    {
        if(usern.equals(""))
        {
            if (passn.equals(""))
            {
                user.setError("EMPTY");
                pass.setError("EMPTY");
                return false;
            }
            else
            {
                user.setError("EMPTY");
                return false;
            }
        }
        else
        {
            if (passn.equals(""))
            {
                pass.setError("EMPTY");
                return false;
            }
            else
            {
                return true;
            }
        }

    }



    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected){
            if(userSession.getInternet()){
                Intent intent= new Intent(MainActivity.this,Attendance_list.class);
                startActivity(intent);
                Animatoo.animateCard(MainActivity.this);
                userSession.setInternet(String.valueOf(true));
            }

        }
        else {
            Intent intent= new Intent(MainActivity.this,Main2Activity.class);
            intent.putExtra("activity","login");
            startActivity(intent);
            Animatoo.animateCard(MainActivity.this);
        }
    }


}
