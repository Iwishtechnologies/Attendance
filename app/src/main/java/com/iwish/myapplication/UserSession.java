// session class to store confidential information

package com.iwish.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;

/**
 * Created by rrdreamtechnology on 18/01/2020.
 */

public class UserSession {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // Sharedpref file name
    private static final String TRACKID = "trackid";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // user avatar (make variable public to access from outside)
    public static final String KEY_PHOTO = "photo";

    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    // check first time app launch
    public static final String SUPERVISORID = "supervisorid";

    // check first time app launch
    public static final String SUPERVISORNAME = "supervisorname";


    // check first time app launch
    public static final String INTERNET = "internet";


    // check first time app launch
    public static final String HOSPITALID = "hospitalid";

    public static final String INSTATUS = "instatus";

    public static final String PHOTO = "photo";

    public static final String HOSPITALNAME = "hospitalname";

    // Constructor
    public UserSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession( String id ,String name, String hospital ,String photo,String Hname){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

//         Storing name in pref
        editor.putString(SUPERVISORID, id);

  //       Storing driverid in pref
        editor.putString(SUPERVISORNAME, name);

        // Storing phone number in pref
        editor.putString(HOSPITALID, hospital);
        editor.putString(PHOTO, photo);
        editor.putString(HOSPITALNAME, Hname);
        // storing duty status in pref
//        editor.putString(DUTY_STATUS,"false");

        // Storing image url in pref
//        editor.putString(KEY_PHOTO, photo);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){

            Intent intent= new Intent(context,Attendance_list.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(SUPERVISORID, pref.getString(SUPERVISORID, null));

        // user email id
        user.put(SUPERVISORNAME, pref.getString(SUPERVISORNAME, null));

        // user phone number
        user.put(HOSPITALID, pref.getString(HOSPITALID, null));
        user.put(PHOTO, pref.getString(PHOTO, null));
        user.put(HOSPITALNAME, pref.getString(HOSPITALNAME, null));

        // user avatar
//        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null)) ;

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public Boolean getFirstTime() {
        return pref.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean n){
        editor.putBoolean(FIRST_TIME,n);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /**
     *  get user mobile
     * **/
    public String get_mobile() {
        String usename = pref.getString("mobile","");
        return usename;
    }


    public HashMap<String, String>setworkerlist(){
        HashMap<String, String> user = new HashMap<>();
        // user avatar
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null)) ;
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null)) ;
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null)) ;

        // return user
        return user;
    }


    public Boolean getInternet() {

        return Boolean.valueOf(pref.getString(INTERNET,""));
    }

    public void setInternet(String string) {
       editor.putString(INTERNET,string).commit();
    }


    public Boolean getinstatus() {

        return Boolean.valueOf(pref.getString(INSTATUS,""));
    }

    public void setinstatus(String string) {
        editor.putString(INSTATUS,string).commit();
    }
}