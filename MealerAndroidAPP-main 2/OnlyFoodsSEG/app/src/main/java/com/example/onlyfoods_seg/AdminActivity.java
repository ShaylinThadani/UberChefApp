package com.example.onlyfoods_seg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    Button adminLogout;
    ListView lvComplaints;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Complaint> complaints = new ArrayList<>();
    String chefID = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminLogout = findViewById(R.id.btnLogoutAdmin);
        lvComplaints= (ListView) findViewById(R.id.lvComplaints);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        lvComplaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                chefID = complaints.get(i).getId();

                showUpdateDeleteDialog(complaint.getComplaintId(), complaint.getChefName());
                return true;
            }
        });




        adminLogout.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(AdminActivity.this, LoginUserActivity.class));
        });

    }
    private void generateList(){

        complaints.clear();
        lvComplaints.setAdapter(null);
      //  lvComplaints.no

        db.collection("complaints")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data = document.getData();
                                Complaint complaint = new Complaint((String) data.get("id"), (String) data.get("chefName"), (String) data.get("description"), document.getId());
                                complaints.add(complaint);
                            }
                        }
                       // Toast.makeText(AdminActivity.this, Integer.toString(complaints.size()), Toast.LENGTH_SHORT).show();
                        ComplaintList complaintsAdapter = new ComplaintList(AdminActivity.this, complaints);
                        lvComplaints.setAdapter(complaintsAdapter);

                    }
                });
    }

    private void showUpdateDeleteDialog(final String complaintId, String chefName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.suspend_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button btnDismiss = (Button) dialogView.findViewById(R.id.btnDismiss);
        final Button btnTemp = (Button) dialogView.findViewById(R.id.btnTemp);
        final Button btnPerm = (Button) dialogView.findViewById(R.id.btnPerm);

        dialogBuilder.setTitle(chefName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        //Dismiss
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("complaints").document(complaintId).delete();
                b.dismiss();
                Toast.makeText(AdminActivity.this,"Successfully Dismissed", Toast.LENGTH_SHORT).show();
            }
        });
        //Ban
        btnPerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference docRef = db.collection("complaints").document(complaintId);
                docRef
                        .update("status", "exiled")


                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Toast.makeText(AdminActivity.this,"Successfully Banned", Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                              //  Toast.makeText(AdminActivity.this,"Error while trying to ban user", Toast.LENGTH_SHORT).show();
                            }
                        });



                DocumentReference docRefUsers = db.collection("users").document(chefID);

                docRefUsers
                        .update("status", "exiled")

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(AdminActivity.this,"Successfully Banned", Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this,"Error while trying to ban user", Toast.LENGTH_SHORT).show();
                            }
                        });

                b.dismiss();
        }








        });


        //Temp Ban
            btnTemp.setOnClickListener(new View.OnClickListener() {

                long timestamp = (System.currentTimeMillis()/1000) + 86400;





                @Override
                public void onClick(View view) {

                    DocumentReference docRef = db.collection("complaints").document(complaintId);
                    docRef
                            .update("status", Long.toString(timestamp))


                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Toast.makeText(AdminActivity.this,"Successfully suspended for one day", Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                     // Toast.makeText(AdminActivity.this,"Error while trying to suspend user", Toast.LENGTH_SHORT).show();
                                }
                            });



                    DocumentReference docRefUsers = db.collection("users").document(chefID);

                    docRefUsers
                            .update("status", Long.toString(timestamp))

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(AdminActivity.this,"Successfully suspended for one day", Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminActivity.this,"Error while trying to suspend user", Toast.LENGTH_SHORT).show();
                                }
                            });

                    b.dismiss();
                }


            });
        }



    protected void onStart() {
        super.onStart();
        db.collection("complaints")
                .whereEqualTo("status", "pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                          //  Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        generateList();
                    }
                });

    }
}