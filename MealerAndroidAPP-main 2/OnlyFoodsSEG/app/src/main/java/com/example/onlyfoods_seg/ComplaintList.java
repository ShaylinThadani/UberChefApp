package com.example.onlyfoods_seg;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintList  extends ArrayAdapter<Complaint> {
    private Activity context;
    private List<Complaint> complaints;



    public ComplaintList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.layout_complaint_list, complaints);
        this.context = context;
        this.complaints = complaints;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_complaint_list, null, true);

        TextView tvChefName = (TextView) listViewItem.findViewById(R.id.tvChefName);
        TextView tvDescription = (TextView) listViewItem.findViewById(R.id.tvDescription);

        Complaint complaint = complaints.get(position);
        tvChefName.setText(complaint.getChefName());
        tvDescription.setText(complaint.getDescription());
        return listViewItem;
    }





/*        String userID = mAuth.getCurrentUser().getUid();
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

                                //boolean type2 = type instanceof String;
                                //Toast.makeText(MainActivity.this, "Type: " + type, Toast.LENGTH_SHORT).show();
                            }
                            // type = task.getResult().getStr ing("type");
                        }

                    }
                });*/

    }


