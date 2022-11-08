package com.example.onlyfoods_seg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CookActivity extends AppCompatActivity {

    Button btnLogoutCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        btnLogoutCook = findViewById(R.id.btnLogoutCook);
        //Toast.makeText(this, "Cook Info uploaded successfully", Toast.LENGTH_SHORT).show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();



        btnLogoutCook.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(CookActivity.this, LoginUserActivity.class));
        });


    }
}