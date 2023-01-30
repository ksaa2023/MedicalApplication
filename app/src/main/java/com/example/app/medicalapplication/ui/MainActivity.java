package com.example.app.medicalapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.databinding.ActivityMainBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.helper.UserSession;
import com.example.app.medicalapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


    }

    private boolean checkData() {
        String userName = binding.userName.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signIn() {
        action="signIn";
        if (checkData()) {
            String userName = binding.userName.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            DialogHelper dialogHelper = new DialogHelper(this);
            dialogHelper.show("Please wait");
            FirebaseFirestore.getInstance().collection("users").
                    document(userName).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            dialogHelper.hide();
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    UserModel userModel = task.getResult().toObject(UserModel.class);
                                    if (userModel.getPassword().equals(password)) {
                                        String type = userModel.getType();
                                        UserSession userSession=new UserSession(MainActivity.this);
                                        userSession.setUserName(userName);
                                        if (type.equals("doctor")) {
                                            Intent intent=new Intent(MainActivity.this,ViewOrdersActivity.class);
                                            intent.putExtra("mode",2);
                                            startActivity(intent);
                                        } else if(type.equals("user")){
                                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                                        }else{
                                            Intent intent=new Intent(MainActivity.this,ViewOrdersActivity.class);
                                            intent.putExtra("mode",3);
                                            startActivity(intent);
                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Error Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void signUp() {

        Intent intent=new Intent(this,CreateAccountActivity.class);
        startActivity(intent);
    }
}