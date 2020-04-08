package com.iwish.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.iwish.myapplication.config.Constants;
import com.iwish.myapplication.connection.ConnectionServer;
import com.iwish.myapplication.connection.JsonHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mindorks.paracamera.Camera;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Takepicture extends AppCompatActivity {
    private Camera camera;
    public static final int REQUSET_IMAGE_CAPTURE = 1234;
    ImageView picture;
    private KProgressHUD kProgressHUD;
    private String encoded_string;
    private String selectedImagePath;
    public ContentResolver cs;
    UserSession userSession;
    String workerid,inout;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        kProgressHUD= new KProgressHUD(Takepicture.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        intent= getIntent();
        workerid= intent.getStringExtra("worker");
        inout= intent.getStringExtra("in/out");
        userSession = new UserSession(Takepicture.this);
        picture = findViewById(R.id.image);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUSET_IMAGE_CAPTURE);

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("data", String.valueOf(data));
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {


            final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            format.setTimeZone(c.getTimeZone());
            String myFormattedtime = format.format(c.getTime());
            Date d = new Date();
            CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
            Log.e("time",myFormattedtime +"    "+ s);



            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (bitmap != null) {
            Canvas canvas= new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(10);
            paint.setColor(getResources().getColor(android.R.color.white));
//            paint.setStrokeWidth(20);
//            paint.setStyle(Paint.Style.STROKE);
            canvas.drawText("Date: " + s+"  Time:"+ myFormattedtime, 0, bitmap.getHeight() - 10, paint);


                picture.setImageBitmap(mutableBitmap);

//                 CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), mutableBitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                Log.e("data", String.valueOf(finalFile));
                upload(workerid,userSession.getUserDetails().get("supervisorid"), String.valueOf(finalFile), String.valueOf(s),myFormattedtime,inout);

            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        camera.deleteImage();
//    }

    public void upload(String workerId, String Supervisorid, final String image, String  date, String time , final String inout){
        setProgressDialog("uploading ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.UPLOAD);
            connectionServer.buildParameter("wid",workerId);
        connectionServer.buildParameter("sid",Supervisorid);
        connectionServer.buildParameter("date",date);
        connectionServer.buildParameter("time",time);
        connectionServer.setFilepath("image",image);
        connectionServer.buildParameter("in/out",inout);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
                        if(inout.equals("Live")){
                            Toast.makeText(Takepicture.this,"Succesfully send Live:", Toast.LENGTH_SHORT).show();

                        }
                        File file = new File(image);
                        file.delete();
                        finish();
                       onBackPressed();
//                        Animatoo.animateSplit(Takepicture.this);
                    }
                    else
                    {
                        remove_progress_Dialog();
                        Toast.makeText(Takepicture.this,"failed to upload", Toast.LENGTH_SHORT).show();
                        finish();
                        onBackPressed();

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


    public void onBackPressed() {
        super.onBackPressed();
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

}
