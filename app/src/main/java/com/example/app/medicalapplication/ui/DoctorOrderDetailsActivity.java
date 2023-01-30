package com.example.app.medicalapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.adapter.PharmacyAdapter;
import com.example.app.medicalapplication.databinding.ActivityDoctorOrderDetailsBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.helper.ImageDecoder;
import com.example.app.medicalapplication.model.Order;
import com.example.app.medicalapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorOrderDetailsActivity extends AppCompatActivity {
    private ActivityDoctorOrderDetailsBinding binding;
    private Order order;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        order = (Order) getIntent().getSerializableExtra("data");
        if (order != null) {
            binding.address.setText("Address: " + order.getAddress());
            binding.name.setText("Name: " + order.getName());
            binding.phone.setText("Phone: " + order.getPhone());
            binding.describe.setText("Describe: " + order.getDescribe());

            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dexter.withContext(DoctorOrderDetailsActivity.this)
                            .withPermission(Manifest.permission.CAMERA)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    openCamera();
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {


                                }
                            }).check();
                }
            });
            binding.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pPhone = binding.etPhone.getText().toString();
                    if (pPhone.isEmpty()) {
                        Toast.makeText(DoctorOrderDetailsActivity.this, "Please Enter pharmacy Phone", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bitmap == null) {
                        Toast.makeText(DoctorOrderDetailsActivity.this, "Please Capture Image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DialogHelper dialogHelper = new DialogHelper(DoctorOrderDetailsActivity.this);
                    dialogHelper.show("please wait");
                    order.setPharmacyName(userModel.getName());
                    order.setPharmacyPhone(pPhone);
                    order.setPharmacyUserName(userModel.getUserName());
                    order.setImage(ImageDecoder.encodedImage(bitmap));
                    FirebaseFirestore.getInstance().collection("orders").document(order.getId()).set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialogHelper.hide();
                            if(task.isSuccessful()){
                                Toast.makeText(DoctorOrderDetailsActivity.this, "Order Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(DoctorOrderDetailsActivity.this, "error try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAll();
    }

    private void getAll() {
        DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.show("please wait");
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dialogHelper.hide();
                if (task.isSuccessful()) {
                    List<UserModel> userModels = task.getResult().toObjects(UserModel.class);
                    List<UserModel> models = new ArrayList<>();
                    for (UserModel model : userModels) {
                        if (model.getType().equals("pharmacy")) {
                            models.add(model);
                        }
                    }
                    if (models.isEmpty()) {
                        Toast.makeText(DoctorOrderDetailsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                        PharmacyAdapter pharmacyAdapter = new PharmacyAdapter(models);
                        binding.spinner.setAdapter(pharmacyAdapter);
                        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                userModel = models.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(DoctorOrderDetailsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 111);
        }
    }

    private Bitmap bitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            binding.image.setImageBitmap(bitmap);

        }
    }
}