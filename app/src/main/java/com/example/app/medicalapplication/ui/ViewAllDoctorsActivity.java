package com.example.app.medicalapplication.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.adapter.DoctorsAdapter;
import com.example.app.medicalapplication.databinding.ActivityViewAllDoctorsBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllDoctorsActivity extends AppCompatActivity {
    private ActivityViewAllDoctorsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewAllDoctorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override
    protected void onStart() {
        super.onStart();
        DialogHelper dialogHelper=new DialogHelper(this);
        dialogHelper.show("Please Wait");
        binding.doctorsLis.setAdapter(null);
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              dialogHelper.hide();
              if(task.isSuccessful()){
                  List<UserModel> userModels = task.getResult().toObjects(UserModel.class);
                  List<UserModel> models=new ArrayList<>();
                  for (UserModel model:userModels){
                      if(model.getType().equals("doctor")){
                          models.add(model);
                      }
                  }
                  if(models.isEmpty()){
                      Toast.makeText(ViewAllDoctorsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                  }else {
                      DoctorsAdapter doctorsAdapter=new DoctorsAdapter(ViewAllDoctorsActivity.this);
                      doctorsAdapter.submitList(models);
                      binding.doctorsLis.setAdapter(doctorsAdapter);
                  }
              }else{

              }
            }
        });
    }
}