package com.example.app.medicalapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app.medicalapplication.R;
import com.example.app.medicalapplication.databinding.ActivityCreateAccountBinding;
import com.example.app.medicalapplication.helper.DialogHelper;
import com.example.app.medicalapplication.helper.UserSession;
import com.example.app.medicalapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {
    private ActivityCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateAccountBinding.inflate(getLayoutInflater());
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

        binding.radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.doctor){
                    binding.spec.setVisibility(View.VISIBLE);
                }else{
                    binding.spec.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean checkData() {
        String name = binding.name.getText().toString().trim();
        String userName = binding.userName.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String spec = binding.spec.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter  Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userName.isEmpty()) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.radioType.getCheckedRadioButtonId()== R.id.doctor && spec.isEmpty()){
            Toast.makeText(this, "Enter Specification", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signIn() {
       finish();
    }

    private void signUp() {

        if (checkData()) {
            String userName = binding.userName.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String specification = binding.spec.getText().toString().trim();
            String name = binding.name.getText().toString().trim();
            DialogHelper dialogHelper = new DialogHelper(this);
            dialogHelper.show("Please wait");
            FirebaseFirestore.getInstance().collection("users").
                    document(userName).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    dialogHelper.hide();
                                    Toast.makeText(CreateAccountActivity.this, "User Name Already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    UserModel userModel;
                                    if(binding.radioType.getCheckedRadioButtonId()==R.id.doctor){
                                        userModel = new UserModel(userName, password, "doctor");
                                        userModel.setName(name);
                                        userModel.setSpec(specification);
                                    }else if(binding.radioType.getCheckedRadioButtonId()==R.id.pharmacy){
                                        userModel = new UserModel(userName, password, "pharmacy");
                                        userModel.setName(name);
                                    }else{
                                        userModel = new UserModel(userName, password, "user");
                                        userModel.setName(name);
                                    }

                                    FirebaseFirestore.getInstance().collection("users").
                                            document(userName).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialogHelper.hide();
                                                    if (task.isSuccessful()) {
                                                        UserSession userSession=new UserSession(CreateAccountActivity.this);
                                                        userSession.setUserName(userName);
                                                        if(binding.radioType.getCheckedRadioButtonId()==R.id.doctor){
                                                            Intent intent=new Intent(CreateAccountActivity.this,ViewOrdersActivity.class);
                                                            intent.putExtra("mode",2);
                                                            finish();

                                                        }else if(binding.radioType.getCheckedRadioButtonId()==R.id.pharmacy){
                                                            Intent intent=new Intent(CreateAccountActivity.this,ViewOrdersActivity.class);
                                                            intent.putExtra("mode",3);
                                                            startActivity(intent);                                                            finish();
                                                        }else{
                                                            startActivity(new Intent(CreateAccountActivity.this, UserActivity.class));
                                                            finish();
                                                        }

                                                    } else {
                                                        Toast.makeText(CreateAccountActivity.this, "Error Try Again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                dialogHelper.hide();
                                Toast.makeText(CreateAccountActivity.this, "Error Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}