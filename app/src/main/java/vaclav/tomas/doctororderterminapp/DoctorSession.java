package vaclav.tomas.doctororderterminapp;

import android.content.Context;
import android.content.SharedPreferences;

public class DoctorSession {

    private static final String PREF_NAME = "login";
    private static final String KEY_IS_LOGGED_IN = "isLoggedin";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public DoctorSession(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedin(boolean isLoggedin){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedin);
        editor.apply();
    }

    public boolean isDoctorLoggedin(){ return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false); }

}
