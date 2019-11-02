package com.rechargeweb.rechargeweb.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.FileUtils;
import com.rechargeweb.rechargeweb.Model.AepsLogIn;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.UploadKycViewModel;
import com.rechargeweb.rechargeweb.databinding.ActivityUploadKycBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class UploadKycActivity extends AppCompatActivity {

    ActivityUploadKycBinding uploadKycBinding;
    private static final int RC_ADHAR_IMAGE = 1;
    private static final int RC_PAN_IMAGE = 2;
    Uri adharUri,panUri;
    String aadharImageUrl, panImageUrl;

    String name,shopName,dob,email,address,pincode,state,mobile,city,adharNo,panNo;
    UploadKycViewModel uploadKycViewModel;
    String session_id;
    String auth_key;
    File adharFile, panFile;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private static final String TAG = UploadKycActivity.class.getSimpleName();

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 99;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    MY_PERMISSIONS_REQUEST_READ_STORAGE
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_kyc);

        //Getting fireabse storage instance
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("kyc_photo");

        verifyStoragePermissions(this);


        auth_key = getResources().getString(R.string.auth_key);

        uploadKycBinding = DataBindingUtil.setContentView(this,R.layout.activity_upload_kyc);
        uploadKycViewModel = ViewModelProviders.of(this).get(UploadKycViewModel.class);

        //Upload aadhar image
        uploadKycBinding.kycAadhaarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_ADHAR_IMAGE);
            }
        });
        //Getting Intent
        Intent intent = getIntent();
        session_id = intent.getStringExtra(Constants.SESSION_ID);
        AepsLogIn aepsLogIn = intent.getParcelableExtra(Constants.AEPS_STATUS);
        if (aepsLogIn.getStatus().equals("Rejected")){

            String reject = "Application Rejected : " + aepsLogIn.getRemark();
            uploadKycBinding.remarkTv.setText(reject);
        }

        //Upload pan image
        uploadKycBinding.kycPanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PAN_IMAGE);
            }
        });

        //Set Date of Birth
        uploadKycBinding.kycDobTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length()==2){
                    uploadKycBinding.kycDobTextInput.setText( s + "/");
                }else if (s.toString().length() == 5){
                    uploadKycBinding.kycDobTextInput.setText(s+"/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Submit button click
        uploadKycBinding.submitKycButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uploadKycBinding.kycNameTextInput.getText().toString().isEmpty()){
                    uploadKycBinding.kycNameLayout.setError("Enter Name");
                }else if (uploadKycBinding.kycShopNameTextInput.getText().toString().isEmpty()){
                    uploadKycBinding.kycShopNameLayout.setError("Enter Shop Name");
                }else if (uploadKycBinding.kycDobTextInput.getText().toString().isEmpty()){
                    uploadKycBinding.kycDobLayout.setError("Enter Date of birth");
                }else if (uploadKycBinding.kycEmailTextInput.getText().toString().isEmpty()){
                    uploadKycBinding.kycEmailLayout.setError("Enter Email Address");
                }else if (uploadKycBinding.kycAddressInputText.getText().toString().isEmpty()){
                    uploadKycBinding.kycAddressLayout.setError("Enter Address");
                }else if (uploadKycBinding.kycPincodeInputText.getText().toString().isEmpty()){
                    uploadKycBinding.kycPincodeLayout.setError("Enter Pincode");
                }else if (uploadKycBinding.kycStateInputText.getText().toString().isEmpty()){
                    uploadKycBinding.kycStateLayout.setError("Enter State Name");
                }else if (uploadKycBinding.kycMobileInputText.getText().toString().isEmpty() || uploadKycBinding.kycMobileInputText.getText().toString().length()<10){
                    uploadKycBinding.kycMobileNumberLayout.setError("Enter Valid Mobile Number");
                }else if (uploadKycBinding.kycCityInputText.getText().toString().isEmpty()){
                    uploadKycBinding.kycCityLayout.setError("Enter City Name");
                }else if (uploadKycBinding.kycAadhaarInputText.getText().toString().isEmpty() || uploadKycBinding.kycAadhaarInputText.getText().toString().length()<12){
                    uploadKycBinding.kycAdharnumberLayout.setError("Enter Valid Aadhaar Number");
                }else if (uploadKycBinding.kycPanNumberInputText.getText().toString().isEmpty()){
                    uploadKycBinding.kycPanNumberLayout.setError("Enter Pan Number");
                }else if (adharUri == null){
                    Toast.makeText(getApplicationContext(),"Upload Aadhar Image",Toast.LENGTH_SHORT).show();
                }else if (panUri == null){
                    Toast.makeText(getApplicationContext(),"Upload Pan Image", Toast.LENGTH_SHORT).show();
                }else {

                    View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UploadKycActivity.this);
                    builder.setCancelable(false);
                    builder.setView(layout);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    name = uploadKycBinding.kycNameTextInput.getText().toString();
                    shopName = uploadKycBinding.kycShopNameTextInput.getText().toString();
                    dob = uploadKycBinding.kycDobTextInput.getText().toString();
                    email = uploadKycBinding.kycEmailTextInput.getText().toString();
                    address = uploadKycBinding.kycAddressInputText.getText().toString();
                    pincode = uploadKycBinding.kycPincodeInputText.getText().toString();
                    state = uploadKycBinding.kycStateInputText.getText().toString();
                    mobile = uploadKycBinding.kycMobileInputText.getText().toString();
                    city = uploadKycBinding.kycCityInputText.getText().toString();
                    adharNo = uploadKycBinding.kycAadhaarInputText.getText().toString();
                    panNo = uploadKycBinding.kycPanNumberInputText.getText().toString();

                    uploadKycViewModel.submitKyc(session_id,auth_key,name,shopName,dob,email,address,pincode,state,mobile,city,adharNo,panNo,aadharImageUrl,panImageUrl)
                            .observe(UploadKycActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {

                            dialog.dismiss();
                            if (!s.isEmpty()){
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                                AlertDialog.Builder aepsBuilder = new AlertDialog.Builder(UploadKycActivity.this);
                                builder.setCancelable(false);

                                builder.setMessage(s);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                AlertDialog dialog1 = aepsBuilder.create();
                                dialog1.show();
                            }else {
                                Toast.makeText(getApplicationContext(),"No response",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Get Aadhaar Image
        if (requestCode == RC_ADHAR_IMAGE && resultCode == RESULT_OK){
            adharUri = data.getData();

            if (adharUri != null) {
                View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setView(layout);

                final AlertDialog dialog = builder.create();
                dialog.show();
                final StorageReference imgaeRef = storageReference.child(adharUri.getLastPathSegment());
                imgaeRef.putFile(adharUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgaeRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        aadharImageUrl = task.getResult().toString();
                        Picasso.get().load(adharUri).placeholder(R.drawable.add_image_icon).into(uploadKycBinding.kycAadhaarImageView);
                        Log.e(TAG,"Aadhaar Uri: " + aadharImageUrl);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to Upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"Upload Failure: " + e.getMessage());
                    }
                });
            }

            //Get Pan Image
        }else if (requestCode == RC_PAN_IMAGE && resultCode == RESULT_OK){
            panUri = data.getData();

            if (panUri!= null) {

                View view = getLayoutInflater().inflate(R.layout.loading_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.show();

                final StorageReference panref = storageReference.child(panUri.getLastPathSegment());
                panref.putFile(panUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return panref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        panImageUrl = task.getResult().toString();
                        Picasso.get().load(panUri).placeholder(R.drawable.add_image_icon).into(uploadKycBinding.kycPanImageView);
                        Log.e(TAG, "Pan url: " + panImageUrl);
                        if (panImageUrl != null){
                            uploadKycBinding.kycInputGroup.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Upload Failure: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Upload failure " + e.getMessage());
                    }
                });

            }
        }else if (resultCode == RESULT_CANCELED){
            Log.e(TAG,"No image selected");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_STORAGE){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                }

            } else {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("We need to access your Location, Please allow location Permission");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(UploadKycActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_STORAGE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        }
}
