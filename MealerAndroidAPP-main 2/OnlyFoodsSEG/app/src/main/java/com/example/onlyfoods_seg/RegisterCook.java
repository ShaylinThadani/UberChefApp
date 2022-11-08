package com.example.onlyfoods_seg;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Map;

public class RegisterCook extends AppCompatActivity {
    // First Name
    EditText etFirstName;
    // Last Name
    EditText etLastName;
    // Address
    EditText etAddress;
    // Upload Button
    Button btnUploadVoid;
    //Register Button
    Button btnRegisterCook;
    //Edit Text Description
    EditText etCookDescription;
    // One Preview Image
    ImageView imgVoid;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    //Firebase Auth
    FirebaseAuth mAuth;
    //Firestore
    FirebaseFirestore fStore;
    //filepath
    Uri filePath;
    //UserID
    String UserID;
    //Cloud Storage for photos
    StorageReference storageRef;
    StorageReference chequesRef;
    //image
    Uri selectedImageUri;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cook);

        //Assign Auth
        mAuth = FirebaseAuth.getInstance();
        // register the UI widgets with their appropriate IDs
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        btnUploadVoid = findViewById(R.id.btnUploadVoid);
        imgVoid = findViewById(R.id.imgVoid);
        btnRegisterCook = findViewById(R.id.btnRegisterCook);
        etCookDescription = findViewById(R.id.etCookDescription);
        // handle the Choose Image button to trigger
        // the image chooser function
        btnUploadVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        btnRegisterCook.setOnClickListener(view ->{
            if(textIsEmpty(etCookDescription) || textIsEmpty(etFirstName) || textIsEmpty(etLastName) || textIsEmpty(etAddress)){
                Toast.makeText(this, "Please fill in all text fields", Toast.LENGTH_SHORT).show();
            }
            else if (hasImage(imgVoid) == false) {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadCookData();
                uploadImage();
                startActivity(new Intent(RegisterCook.this, CookActivity.class));
            }
        });
    }
    private void uploadImage(){
        storage = FirebaseStorage.getInstance();
        String extension = getFileExtension(selectedImageUri);
        String userID = mAuth.getCurrentUser().getUid();
        storageRef = storage.getReference();
        chequesRef = storageRef.child("Cheques/" +userID + extension);

        imgVoid.setDrawingCacheEnabled(true);
        imgVoid.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgVoid.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(extension.equals("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        else{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = chequesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RegisterCook.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

/*
    private void uploadImage() {
        String fileName = UserID + "_void_cheque";
        btnRegisterCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

                storageReference.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgVoid.setImageURI(null);
                                Toast.makeText(RegisterCook.this, "Succssfully uploaded information", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterCook.this, "Failed to upload information", Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });
    }
*/
    private boolean textIsEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    private boolean hasImage(ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imgVoid.setImageURI(selectedImageUri);
                }
            }
        }
    }

    protected void uploadCookData() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String address = etAddress.getText().toString();
        String userID = mAuth.getCurrentUser().getUid();
        Map<String,Object> user = new HashMap<>();
        user.put("type","Chef");
        user.put("first name", firstName);
        user.put("last name", lastName);
        user.put("address", address);
        user.put("status", "active");
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("users").document(userID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Added chef info for " + userID);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Failed adding chef info for " + userID, e);
                    }
                });
    }





}


