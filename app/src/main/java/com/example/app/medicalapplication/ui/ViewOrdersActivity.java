package com.example.app.medicalapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.adapter.OrdersAdapter;
import com.example.app.medicalapplication.databinding.ActivityViewOrdersBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.helper.UserSession;
import com.example.app.medicalapplication.model.Order;
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
import java.util.HashMap;
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    private ActivityViewOrdersBinding binding;
    private int mode;
    private UserSession userSession;
    private DialogHelper dialogHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mode=getIntent().getIntExtra("mode",0);
        userSession=new UserSession(this);
        dialogHelper=new DialogHelper(this);
        this.setTitle("All Orders");



    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllOrders();
    }

    public void getAllOrders(){
        dialogHelper.show("get Orders Data");
        binding.emptyData.setVisibility(View.GONE);
        binding.ordersList.setAdapter(null);
        FirebaseFirestore.getInstance().collection("orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dialogHelper.hide();
               if(task.isSuccessful()){
                   List<Order> orders = task.getResult().toObjects(Order.class);
                   if(orders.isEmpty()){
                       binding.emptyData.setVisibility(View.VISIBLE);
                       binding.ordersList.setAdapter(null);
                   }else{
                       List<Order> list=new ArrayList<>();
                       for (Order order : orders) {
                           if(mode==1){
                               if(order.getUserName().equals(userSession.gtUserName())){
                                   list.add(order);
                               }
                           }

                           if(mode==2){
                               if(order.getDocUserName().equals(userSession.gtUserName())){
                                   list.add(order);
                               }
                           }

                           if(mode==3){
                               if(order.getPharmacyUserName().equals(userSession.gtUserName())){
                                   list.add(order);
                               }
                           }

                       }
                       if(list.isEmpty()){
                           binding.emptyData.setVisibility(View.VISIBLE);
                           binding.ordersList.setAdapter(null);
                       }else{
                           //set Adapter
                           OrdersAdapter ordersAdapter=new OrdersAdapter(ViewOrdersActivity.this,mode);
                           ordersAdapter.submitList(list);
                           binding.ordersList.setAdapter(ordersAdapter);
                       }

                   }
               }else{
                   binding.emptyData.setVisibility(View.VISIBLE);
                   Toast.makeText(ViewOrdersActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });
    }






    AlertDialog dialog=null;
}