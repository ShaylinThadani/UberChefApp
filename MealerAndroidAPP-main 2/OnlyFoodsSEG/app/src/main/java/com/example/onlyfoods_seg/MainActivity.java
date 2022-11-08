package com.example.onlyfoods_seg;
import com.example.onlyfoods_seg.LoginUserActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

  //  Button btnLogOut;
    FirebaseAuth mAuth;
    String type = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // btnLogOut = findViewById(R.id.btnLogoutAdmin);
        mAuth = FirebaseAuth.getInstance();







    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();



        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginUserActivity.class));

        }
        else{
            identifyUser(user);
        }



    }

    public void identifyUser(FirebaseUser user){
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
                                String status = task.getResult().getString("status");
                                chooseUser(type, status);
                                //boolean type2 = type instanceof String;
                                //Toast.makeText(MainActivity.this, "Type: " + type, Toast.LENGTH_SHORT).show();
                            }
                            // type = task.getResult().getStr ing("type");
                        }

                    }
                });
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public void chooseUser(String type,String status) {
        if (status.equals("exiled")){
            Toast.makeText(MainActivity.this, "This user is banned" ,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginUserActivity.class));

        }
        else if (isNumeric(status) )
        {
          long currentTime = System.currentTimeMillis()/1000;
          if (Integer.valueOf(status) > currentTime) {
              Toast.makeText(MainActivity.this, "This user is suspended for " + (int)Math.round((Integer.valueOf(status) - currentTime)/3600.0) + " hours", Toast.LENGTH_SHORT).show();

              startActivity(new Intent(MainActivity.this, LoginUserActivity.class));

          }
              else{
              DocumentReference docRefUsers = db.collection("users").document(mAuth.getCurrentUser().getUid());
              docRefUsers
                          .update("status", "active");
              startActivity(new Intent(MainActivity.this, CookActivity.class));
              Toast.makeText(MainActivity.this, "Ban Lifted. Welcome back!", Toast.LENGTH_SHORT).show();


          }





        }
        else {
            if (type.equals("Chef")) {
                startActivity(new Intent(MainActivity.this, CookActivity.class));
            } else if (type.equals("Admin")) {
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
            } else if (type.equals("Client")) {
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        }
    }

}