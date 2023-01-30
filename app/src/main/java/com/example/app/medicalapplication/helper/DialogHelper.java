package com.example.app.medicalapplication.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogHelper {
    private ProgressDialog dialog;
    public DialogHelper(Context context){
        dialog=new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);

    }
    public void show(String message){
        dialog.setMessage(message);
        dialog.show();
    }

    public void hide(){
        dialog.dismiss();
    }
}
