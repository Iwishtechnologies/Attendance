package com.iwish.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iwish.myapplication.config.Constants;
import com.iwish.myapplication.connection.ConnectionServer;
import com.iwish.myapplication.connection.JsonHelper;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import ir.hamiss.internetcheckconnection.InternetAvailabilityChecker;
import ir.hamiss.internetcheckconnection.InternetConnectivityListener;

public class Feedback extends AppCompatActivity implements InternetConnectivityListener {
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    KProgressHUD kProgressHUD;
    CheckBox point1,point2,point3,point4,point5;
    String one,two,three,four,five ,path;
    EditText Commenmt, mobile,room;
    Button send;
    UserSession userSession;
    ImageView back;
    ArrayList<String>data;
    ArrayList<Integer>id;
    LinearLayout linearLayout;
    SignaturePad mSignaturePad;
    TextView clear,sign;
    File file,finalFile;
    Bitmap mutableBitmap ,bitmap;
    int j;
    public int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);




        userSession = new UserSession(Feedback.this);
        kProgressHUD= new KProgressHUD(Feedback.this);
        Commenmt= findViewById(R.id.comment);
        back= findViewById(R.id.back);
        mobile= findViewById(R.id.mobile);
        send= findViewById(R.id.send);
        linearLayout= findViewById(R.id.linear);
        room= findViewById(R.id.room);
        sign= findViewById(R.id.sign);
        clear= findViewById(R.id.clear);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
        data= new ArrayList<>();
        id= new ArrayList<>();
        feedback();

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                sign.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {

                bitmap=mSignaturePad.getSignatureBitmap();
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Log.e("sign", String.valueOf(bitmap));
//                 Assume block needs to be inside a Try/Catch block.
//                 path = Environment.getExternalStorageDirectory().toString();
//                Log.e("path",path);
//                OutputStream fOut = null;
//                Integer counter = 0;
//                 file = new File(path, "FitnessGirl"+counter+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
//                Log.e("file", String.valueOf(file));
//                try {
//                    fOut = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//                    try {
//                        fOut.flush(); // Not really required
//                        fOut.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                     // do not forget to close the stream
////                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }


        });

         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onBackPressed();
             }
         });

         clear.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mSignaturePad.clear();
                 File fdelete = new File(String.valueOf(file));
                 Log.e("buyuygyuiu", String.valueOf(fdelete.exists()));
                 fdelete.delete();
             }

         });







        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mSignaturePad.isEmpty()))
                {
                   if(validateInput(mobile.getText().toString(),room.getText().toString()))
                   {
                       //                 CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                       Uri tempUri = getImageUri(getApplicationContext(),mutableBitmap);

                       // CALL THIS METHOD TO GET THE ACTUAL PATH
                       finalFile = new File(getRealPathFromURI(tempUri));
                       savefeedback(String.valueOf(data),Commenmt.getText().toString(),mobile.getText().toString(),room.getText().toString(), String.valueOf(finalFile));
                   }

                }
                else {
                    Toast.makeText(Feedback.this, "Signature is Required", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }





    public void feedback(){

        setProgressDialog("Loading ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.FEEDBACKDATA);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @SuppressLint("NewApi")
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                final JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");

                        Log.e("json", String.valueOf(jsonArray));

                        for(i=0;i<jsonArray.length();i++)
                        {

                            jsonHelper.setChildjsonObj(jsonArray,i);

                         final CheckBox  checkBox= new CheckBox(Feedback.this);
                            checkBox.setText(jsonHelper.GetResult("points"));
                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f);
//                            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f));
                            linearLayout.addView(checkBox);
                            id.add(Integer.valueOf(jsonHelper.GetResult("id")));
                           checkBox.setBackgroundResource(R.drawable.feedback_background);
                           params.bottomMargin=30;
                           checkBox.setId(i);
                           checkBox.setContentDescription(jsonHelper.GetResult("points"));
                           checkBox.setButtonTintList(Feedback.this.getResources().getColorStateList(R.color.colorPrimary));
                           checkBox.setLayoutParams(params);

                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if(checkBox.isChecked()){


//                                            Toast.makeText(Feedback.this, ""+checkBox.getId(), Toast.LENGTH_SHORT).show();
//                                    data.add(checkBox.getText().toString());
                                            data.add(String.valueOf(id.get(checkBox.getId())));
                                            Log.e("data", String.valueOf(data));


                                    }


                                }
                            });
//
                        }
                        remove_progress_Dialog();

                    }
                    else
                    {
                        remove_progress_Dialog();
                        Toast.makeText(Feedback.this,""+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();

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


    public void savefeedback(String data, String comment,String mobile,String Room ,String signature){
        setProgressDialog("Saving ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.SAVEFEEDBACK);
        connectionServer.buildParameter("data",data);
        connectionServer.buildParameter("rm",Room);
        connectionServer.buildParameter("cmt",comment);
        connectionServer.buildParameter("mobile",mobile);
        connectionServer.buildParameter("mbl",Room);
        connectionServer.buildParameter("sid",userSession.getUserDetails().get("supervisorid"));
        connectionServer.setFilepath("image",signature);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
                        finalFile.delete();
                        onBackPressed();
//                        Intent intent= new Intent(Feedback.this,Feedback.class);
//                        startActivity(intent);
//                        Animatoo.animateInAndOut(Feedback.this);
                    }
                    else {
                        remove_progress_Dialog();
                    }

                }

            }
        });

    }


    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            mobile.setError("Mobile field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            room.setError("Ward/Room/Place+ is empty.");
            return false;
        }

        return true;
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected){
            Intent intent= new Intent(Feedback.this,Attendance_list.class);

            startActivity(intent);
            Animatoo.animateCard(Feedback.this);
        }
        else {
            Intent intent= new Intent(Feedback.this,Main2Activity.class);
            intent.putExtra("activity","feedback");
            startActivity(intent);
            Animatoo.animateCard(Feedback.this);
        }
    }

}
