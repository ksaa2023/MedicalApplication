package com.example.app.medicalapplication.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.medicalapplication.databinding.ActivityUserViewOrderDetailsBinding;
import com.example.app.medicalapplication.helper.ImageDecoder;
import com.example.app.medicalapplication.model.Order;


public class UserViewOrderDetailsActivity extends AppCompatActivity {
    private ActivityUserViewOrderDetailsBinding binding;
    private Order order;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserViewOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        from=getIntent().getStringExtra("from");
        order=(Order) getIntent().getSerializableExtra("data");
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(order!=null){
            if(from ==null){
                binding.image.setImageBitmap(ImageDecoder.getImageFromString(order.getImage()));
                binding.name.setText("Pharmacy Name :"+order.getPharmacyName());
                binding.phone.setText("Pharmacy Phone :"+order.getPharmacyPhone());
            }else{
                binding.image.setImageBitmap(ImageDecoder.getImageFromString(order.getImage()));
                binding.title.setText("Patient Information");
                binding.describe.setVisibility(View.VISIBLE);
                binding.address.setVisibility(View.VISIBLE);
                binding.address.setText("Address :"+order.getAddress());
                binding.name.setText("Patient Name :"+order.getName());
                binding.phone.setText("Patient Phone :"+order.getPhone());
                binding.describe.setText("Describe :"+order.getDescribe());
                binding.address.setText("Address :"+order.getAddress());
            }

        }

    }
}