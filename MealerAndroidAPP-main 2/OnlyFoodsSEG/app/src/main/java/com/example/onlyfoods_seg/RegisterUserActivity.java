        package com.example.onlyfoods_seg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnChefRegister;
    Button getBtnClientRegister;

    Button btnChef;
    Button btnClient;
    String userType;
    FirebaseFirestore fStore;
    String userID;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);

        btnChefRegister = findViewById(R.id.btnRegisterChef);
        getBtnClientRegister = findViewById(R.id.btnRegisterClient);



        fStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        btnChefRegister.setOnClickListener(view ->{
            userType = "Chef";
            createUserChef();
        });

        getBtnClientRegister.setOnClickListener(view ->{
            userType = "Client";
            createUserClient();
        });




        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterUserActivity.this, LoginUserActivity.class));
        });
    }
    private void createUserClient(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password);
            mAuth.signInWithEmailAndPassword(email,password);
            startActivity(new Intent(RegisterUserActivity.this, RegisterClient.class));
        }
    }
    private void createUserChef(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password);
            mAuth.signInWithEmailAndPassword(email,password);
            startActivity(new Intent(RegisterUserActivity.this, RegisterCook.class));
             }
    }

}