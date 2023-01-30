package com.example.app.medicalapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.databinding.ActivityAddUpdateOrderBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.helper.ImageDecoder;
import com.example.app.medicalapplication.helper.UserSession;
import com.example.app.medicalapplication.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AddUpdateOrderActivity extends AppCompatActivity {
    private ActivityAddUpdateOrderBinding binding;
    private Order order;
    private String docUserName;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddUpdateOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        docUserName= getIntent().getStringExtra("doc");
        order= (Order) getIntent().getSerializableExtra("data");
        if(order!=null){
            fillData(order);
        }
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(AddUpdateOrderActivity.this)
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
              if(checkData()){
                  DialogHelper dialogHelper=new DialogHelper(AddUpdateOrderActivity.this);
                  dialogHelper.show("Please wait");
                  if(order==null){
                      String id= FirebaseFirestore.getInstance().collection("orders").document().getId();
                      String userName=new UserSession(AddUpdateOrderActivity.this).gtUserName();
                      //String image=ImageDecoder.encodedImage(bitmap);
                      String name=binding.name.getText().toString().trim();
                      String address=binding.address.getText().toString().trim();
                      String phone=binding.phone.getText().toString().trim();
                      String describe=binding.descripe.getText().toString().trim();
                      Order newOrder=new Order(id,userName,null,name,address,phone,null,null);
                      newOrder.setDocUserName(docUserName);
                      newOrder.setDescribe(describe);
                      FirebaseFirestore.getInstance().collection("orders").document(id).set(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              dialogHelper.hide();
                              if(task.isSuccessful()){
                                  Toast.makeText(AddUpdateOrderActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                  finish();
                              }else{
                                  Toast.makeText(AddUpdateOrderActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                  }else{
                      // update
                      order.setAddress(binding.address.getText().toString().trim());
                      order.setName(binding.name.getText().toString().trim());
                      order.setPhone(binding.phone.getText().toString().trim());
                      order.setImage(bitmap==null?order.getImage(): ImageDecoder.encodedImage(bitmap));
                      FirebaseFirestore.getInstance().collection("orders").document(order.getId()).set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              dialogHelper.hide();
                              if(task.isSuccessful()){
                                  Toast.makeText(AddUpdateOrderActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                  finish();
                              }else{
                                  Toast.makeText(AddUpdateOrderActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          }
                      });

                  }
              }
            }
        });
    }
    private boolean checkData(){
        String name=binding.name.getText().toString().trim();
        String phone=binding.phone.getText().toString().trim();
        String address=binding.address.getText().toString().trim();
        String describe=binding.address.getText().toString().trim();
        if(name.isEmpty()){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(phone.isEmpty()){
            Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address.isEmpty()){
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(describe.isEmpty()){
            Toast.makeText(this, "Enter Describe", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if(order==null && bitmap==null){
//            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 111);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            binding.image.setImageBitmap(bitmap);

        }
    }

    private void fillData(Order order) {
        binding.address.setText(order.getAddress());
        binding.name.setText(order.getName());
        binding.phone.setText(order.getPhone());
        binding.image.setImageBitmap(ImageDecoder.getImageFromString(order.getImage()));
        binding.pageTitle.setText("update Order");
    }
}