package com.chatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.databinding.ActivityRegisterBinding;
import com.chatapp.pojo.PojoUser;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class RegisterActivity extends AppCompatActivity {
   String user,pass,phone;
    ActivityRegisterBinding binding;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    StorageReference storageReference;
    FirebaseStorage storage;
    String imageUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_register);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding.tvLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        binding.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = binding.etUsername.getText().toString();
                pass = binding.etPassword.getText().toString();
                phone = binding.etPhone.getText().toString();
                if (user.equals("")) {
                    binding.etUsername.setError("Please enter name");
                } else if (pass.equals("")) {
                    binding.etPassword.setError("Please enter password");
                }  else if (pass.length() < 5) {
                    binding.etPassword.setError("At least 5 characters long");
                } else if (phone.length() < 10) {
                    binding.etPhone.setError("At least 10 digits");
                }else {
                    Random rand = new Random();
                    // Generate random integers in range 0 to 999
                    int userId = rand.nextInt(100);
                    PojoUser pojoUser =new PojoUser("no","true","","",user,pass,phone,String.valueOf(userId),imageUrl,"","");
                    Firebase reference = new Firebase("https://chatapp-9dacf.firebaseio.com/user");
                    reference.child(user).child("Detail").setValue(pojoUser);
                    Toast.makeText(RegisterActivity.this, "RegisterActivity successfully..", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage(); }
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference
                    .child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                imageUrl=     taskSnapshot.getDownloadUrl().toString();

                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }

}
