package com.example.onlyfoods_seg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogoutClient;
    FirebaseFirestore fStore;
    FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
       // Toast.makeText(this, "Client Info uploaded successfully", Toast.LENGTH_SHORT).show();

        btnLogoutClient = findViewById(R.id.btnLogoutClient);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        btnLogoutClient.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(ClientActivity.this, LoginUserActivity.class));
        });


    }


}


