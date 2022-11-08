package com.example.onlyfoods_seg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//import com.fevziomurtekin.payview.Payview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RegisterClient extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etFirstName;
    TextInputEditText etLastName;
    TextInputEditText etCardNumber;
    TextInputEditText etExpiryDate;
    TextInputEditText etCVV;
    TextInputEditText etFullAddress;


    Button getBtnClientRegister;
    FirebaseAuth mAuth;


    String userType;
    String firstName;
    String lastName;
    String expiryDate;
    Integer cardNumber;
    Integer cvv;

    String cardNumber2;
    String cvv2;

    String address;


    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        mAuth = FirebaseAuth.getInstance();


        // etRegEmail = findViewById(R.id.etRegEmail);
        //  etRegPassword = findViewById(R.id.etRegPass);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCVV = findViewById(R.id.etCVV);
        etFullAddress = findViewById(R.id.etFullAddress);

        getBtnClientRegister = findViewById(R.id.btnRegisterClient);

        getBtnClientRegister.setOnClickListener(view ->{

            if(textIsEmpty(etFirstName)  || textIsEmpty(etLastName) || textIsEmpty(etCardNumber)){
                Toast.makeText(this, "Please fill in all text fields", Toast.LENGTH_SHORT).show();
            }


            else {
                if(verifyCC(etExpiryDate.getText().toString(), etCVV.getText().toString(), etCardNumber.getText().toString()) == true) {

                    uploadClientData();
                    //
                    //
                    //Upload to DATA to Cloud
                    //
                    startActivity(new Intent(RegisterClient.this, ClientActivity.class));
                }
            }
        });
    }

    private boolean textIsEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    protected void uploadClientData() {

        fStore = FirebaseFirestore.getInstance();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String cardNum = etCardNumber.getText().toString();
        String expiryDate = etExpiryDate.getText().toString();
        String address = etLastName.getText().toString();
        String stringCvv = etCVV.getText().toString();

        String userID = mAuth.getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("type","Client");
        user.put("first name", firstName);
        user.put("last name", lastName);
        user.put("card number", cardNum);
        user.put("expiry date card number", expiryDate);
        user.put("cvv", stringCvv);
        user.put("address", address);

        try {

            fStore.collection("users").document(userID)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "Added client info for " + userID);

                        }
                    });
        }catch(Exception e){
            etCardNumber.setText(e.toString());
        }
    }
    private boolean verifyCC(String expiryDate, String cvvString, String cardNum){
        expiryDate = expiryDate.trim();
        cvvString = cvvString.trim();
        cardNum = cardNum.trim();
        // check format
        if( !(expiryDate.matches("((0[1-9])|(1[0-2]))/\\d{2}")) ){
            Toast.makeText(this, "Invalid Date format", Toast.LENGTH_SHORT).show();
            return false;
        }
        Date expiry = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        try{
            expiry = simpleDateFormat.parse(expiryDate);
        }
        catch(Exception e){
        }
        boolean expired = expiry.before(new Date());
        if (expired == true)
        {
            Toast.makeText(this, "Date is expired", Toast.LENGTH_SHORT).show();
            return false;
        }
        //cvv
        int cvvInt;
        try{
            cvvInt = Integer.parseInt(cvvString);
        }
        catch(Exception e){
            Toast.makeText(this, "CVV must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }
        int cvvLength = cvvString.length();
        if (cvvLength != 3){
            Toast.makeText(this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Credit Card Number
        Long CardNumInt;
        try{
            CardNumInt = Long.parseLong(cardNum);
        }
        catch(Exception e){
            Toast.makeText(this, "Credit Card number must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }

        int cardNumberLength = cardNum.length();
        if (cardNumberLength != 16){
            Toast.makeText(this, "Credit Card number must be 16 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}

