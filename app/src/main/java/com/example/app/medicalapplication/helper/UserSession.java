package com.example.app.medicalapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;


public class UserSession {
    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;
    private Context context;

    public UserSession(Context context){
        this.context=context;
        preferences=context.getSharedPreferences("data",0);
        editor=preferences.edit();
    }
    //id

    public void setUserName(String name){
        editor.putString("name",name).commit();
    }

    public String gtUserName(){
        return preferences.getString("name",null);
    }



}
