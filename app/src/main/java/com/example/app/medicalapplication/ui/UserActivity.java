package com.example.app.medicalapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.medicalapplication.databinding.ActivityUserBinding;


public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,ViewOrdersActivity.class);
                intent.putExtra("mode",1);
                startActivity(intent);
            }
        });
        binding.viewDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,ViewAllDoctorsActivity.class);
                intent.putExtra("mode",1);
                startActivity(intent);
            }
        });



    }
}