package com.example.onlyfoods_seg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginUserActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;
    //boolean isSuspended;
    boolean isSuspended;

    // String type = "";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginUserActivity.this, RegisterUserActivity.class));
        });
    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Toast.makeText(LoginUserActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //Only runs if there is no suspension
                        //if (isSuspended != true) {
                            startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                       // }

                    }


                    else{
                        Toast.makeText(LoginUserActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }


    public boolean checkComplaints(FirebaseUser user){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference;

        reference = firestore.collection("complaints").document(user.getUid());
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {

                              //  String type = task.getResult().getString("suspensionType");
                                String expiryDate = task.getResult().getString("complaintExpiryDate");
                                //boolean type2 = type instanceof String;

                                Toast.makeText(LoginUserActivity.this, "This account has been suspended for until " + expiryDate, Toast.LENGTH_SHORT).show();
                                isSuspended = true;
                            }
                            else {
                                isSuspended = false;

                            }
                        }

                    }
                });

        return isSuspended;
    }

}

   /* public void identifyUser(FirebaseUser user){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference;
        reference = firestore.collection("users").document(user.getUid());
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                String type = task.getResult().getString("type");
                                chooseUser(type);
                                //boolean type2 = type instanceof String;
                                Toast.makeText(LoginUserActivity.this, "Type: " + type, Toast.LENGTH_SHORT).show();
                            }
                            // type = task.getResult().getStr ing("type");
                        }
                    }
                });
    }
    public void chooseUser(String type) {
        Toast.makeText(LoginUserActivity.this, "Type: " + type, Toast.LENGTH_SHORT).show();
         if (type.equals("Chef")){
          startActivity(new Intent(LoginUserActivity.this, CookActivity.class));
          }
          else if (type.equals("Client")){
              startActivity(new Intent(LoginUserActivity.this, ClientActivity.class));
           }
          else {
             startActivity(new Intent(LoginUserActivity.this, AdminActivity.class));
         }
    }*/