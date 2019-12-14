package com.rechargeweb.rechargeweb.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rechargeweb.rechargeweb.BottomSheetFrag.BuyCouponBottomSheetFragment;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Credential;
import com.rechargeweb.rechargeweb.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class PanActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    String session_id, auth;

    private boolean isPanCorrect, isMobileCorrect, isEmailCorrect;

    private LocationManager locationManager;
    private Location location;
    private boolean isGpsEnable;

    //Credential page
    ConstraintLayout credentialPageLayout;
    FancyButton viewCredentialButton;
    FancyButton buyCouponButton;
    FancyButton logInUTIITSL;
    ProgressBar utiProgressBar;
    String message,psaId, userName, status, remark, price;

    //Credential form
    CardView credentialFormlayout;
    EditText credNameEt, credShopNameEt, credLocalityEt, credPincodeEt, credStateEt, credMobileEt, credEmailEt, credPanNoEt;
    Button credSubmitButton, credCancelButton;
    TextView remarkText;
    String name, shop, locality, pincode, state, mobile, email, pan;
    String iLoc, iPin, iState;

    private static final String TAG = PanActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan);

        //Credential page implementation
        credentialPageLayout = findViewById(R.id.credential_page);
        viewCredentialButton = findViewById(R.id.cred_view_button);
        buyCouponButton = findViewById(R.id.cred_buy_coupon);


        //Credential form implementation
        credentialFormlayout = findViewById(R.id.credential_form_layout);
        credNameEt = findViewById(R.id.credential_name);
        credShopNameEt = findViewById(R.id.cred_shop_name);
        credLocalityEt = findViewById(R.id.cred_locality);
        credPincodeEt = findViewById(R.id.cred_pincode);
        credStateEt = findViewById(R.id.cred_state);
        credMobileEt = findViewById(R.id.cred_mobile);
        credEmailEt = findViewById(R.id.cred_email);
        credPanNoEt = findViewById(R.id.cred_pan);
        credSubmitButton = findViewById(R.id.cred_submit_button);
        credCancelButton = findViewById(R.id.cred_cancel_button);
        remarkText = findViewById(R.id.cred_remark);
        logInUTIITSL = findViewById(R.id.log_in_utiisl);
        utiProgressBar = findViewById(R.id.uti_progress_bar);


        //Getting values via intent from HomeActivity
        Intent intent = getIntent();
        //Initializing session id and auth key
        session_id = getIntent().getStringExtra(Constants.SESSION_ID);
        auth = getResources().getString(R.string.auth_key);

        if (intent.hasExtra(Constants.LOCATION_BUNDLE)){

            Bundle bundle = intent.getBundleExtra(Constants.LOCATION_BUNDLE);
            iLoc = bundle.getString(Constants.LOCALITY);
            iPin = bundle.getString(Constants.PINCODE);
            iState = bundle.getString(Constants.STATE);

            if (iLoc == null || iPin == null || iState == null){
                setLocations();
            }else {

                credLocalityEt.setText(iLoc);
                credPincodeEt.setText(iPin);
                credStateEt.setText(iState);
            }
        }

        //Log in to UTIISL
        logInUTIITSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utiProgressBar.setVisibility(View.VISIBLE);
                logInUTIITSL.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        utiProgressBar.setVisibility(View.GONE);
                        logInUTIITSL.setVisibility(View.VISIBLE);
                        String UTIITSL_URL = "https://www.psaonline.utiitsl.com/psaonline/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(UTIITSL_URL));
                        startActivity(intent);
                    }
                },2000);

            }
        });


        //Initializing view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Check credential if user registered or not
        checkCredential(session_id, auth);

        //On View credential button clicked
        viewCredentialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainViewModel.getCredentialDetails(session_id, auth).observe(PanActivity.this, new Observer<Credential>() {
                    @Override
                    public void onChanged(Credential credential) {

                        status = credential.getStatus();
                        remark = credential.getRemark();
                        if (status.equals("Approved")){
                            Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                            showCredentialDialog(credential);
                            price = credential.getPrice();
                        }else if (status.equals("Pending")){
                            Toast.makeText(getApplicationContext(),"Your Registration with UTIITSL is under processing, Try again later",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                            credentialPageLayout.setVisibility(View.GONE);
                            credentialFormlayout.setVisibility(View.VISIBLE);
                            if (remark!= null) {
                                remarkText.setVisibility(View.VISIBLE);
                                remarkText.setText("Rejected Due to " + remark);
                                remarkText.setTextColor(Color.RED);
                            }
                        }

                    }
                });
            }
        });

        //On buy coupon button click
        buyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status != null ) {
                    if (status.equals("Approved")) {
                        showBuyCouponSheet();
                    }else {
                        Toast.makeText(getApplicationContext(),"Your Registration with UTIITSL is under processing, Try again later",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Your Registration with UTIITSL is under processing, Try again later",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setLocations() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                isGpsEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isGpsEnable) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            iLoc = list.get(0).getLocality();
                            iPin = list.get(0).getPostalCode();
                            iState = list.get(0).getAdminArea();

                            credLocalityEt.setText(iLoc);
                            credPincodeEt.setText(iPin);
                            credStateEt.setText(iState);
                            Log.e(TAG, iLoc + ", " + iPin + ", " + iState);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to get Location", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //Create an alert dialog if the location is off on device
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setCancelable(false);
                    builder.setMessage("Your Location is disabled. Turn on your location");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            } else {

                Toast.makeText(getApplicationContext(), "Unable to fetch location Service", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Show buyCoupon Sheet
    private void showBuyCouponSheet(){
        BuyCouponBottomSheetFragment buyCouponBottomSheetFragment = new BuyCouponBottomSheetFragment(psaId,userName,price);
        buyCouponBottomSheetFragment.show(getSupportFragmentManager(),buyCouponBottomSheetFragment.getTag());
    }

    //Check if user is registered or not
    private void checkCredential(String session_id, String auth) {

        mainViewModel.getCredentialDetails(session_id, auth).observe(this, new Observer<Credential>() {
            @Override
            public void onChanged(Credential credential) {

                if (credential != null) {
                    Log.e(TAG,"Credential is not empty");
                    message = credential.getMessage();
                    psaId = credential.getPsaId();
                    userName = credential.getVleName();
                    status = credential.getStatus();
                    remark = credential.getRemark();
                    price = credential.getPrice();
                    if (message.equals("Success") &&(status.equals("Approved" )|| status.equals("Pending"))) {
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        credentialFormlayout.setVisibility(View.GONE);
                        credentialPageLayout.setVisibility(View.VISIBLE);
                    }else if (message.equals("Not Registered") && status.equals("Pending")){
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        credentialFormlayout.setVisibility(View.GONE);
                        credentialPageLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        credentialPageLayout.setVisibility(View.GONE);
                        credentialFormlayout.setVisibility(View.VISIBLE);
                        if ( remark != null && remark.equals("empty remark")) {
                            remarkText.setVisibility(View.VISIBLE);
                            remarkText.setText("Rejected Due to " + remark);
                            remarkText.setTextColor(Color.RED);
                        }
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.e(TAG,"No credential found");
                }
            }
        });

    }

    //Create a dialog to show credential details
    private void showCredentialDialog(Credential credential) {

        View layout = getLayoutInflater().inflate(R.layout.credential_dialog, null);

        TextView psaIdTv = layout.findViewById(R.id.cred_psa_id);
        TextView credNameTv = layout.findViewById(R.id.view_credential_name);
        Button closeButton = layout.findViewById(R.id.credential_close_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        psaIdTv.setText(credential.getPsaId());
        credNameTv.setText(credential.getVleName());
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //On submit button click
    public void onSubmitButtonClick(View view) {

        name = credNameEt.getText().toString().toUpperCase();
        shop = credShopNameEt.getText().toString().toUpperCase();
        locality = credLocalityEt.getText().toString().toUpperCase().trim();
        pincode = credPincodeEt.getText().toString().trim();
        state = credStateEt.getText().toString().toUpperCase();
        mobile = credMobileEt.getText().toString().trim();
        email = credEmailEt.getText().toString().toLowerCase().trim();
        pan = credPanNoEt.getText().toString().toUpperCase().trim();

        isPanCorrect = panCheck(pan);
        isMobileCorrect = mobileCheck(mobile);
        isEmailCorrect = isValidEmail(email);

        //Check if input fields are not empty
        if (!name.isEmpty() && !shop.isEmpty() && !locality.isEmpty() && !pincode.isEmpty() && !state.isEmpty() && !mobile.isEmpty() && !email.isEmpty() && !pan.isEmpty()) {

            //Check if mobile, email and pan is correct
            if (isPanCorrect && isMobileCorrect && isEmailCorrect) {
                Toast.makeText(getApplicationContext(), "correct:  " + pan + "," + mobile + ", " + email, Toast.LENGTH_SHORT).show();
                getPsaResponse(session_id,auth,name,shop,locality,pincode,state,mobile,email,pan);
            } else if (!isMobileCorrect) {
                credMobileEt.setText("");
                Toast.makeText(getApplicationContext(), "mobile is incorrect " + mobile, Toast.LENGTH_SHORT).show();
            } else if (!isEmailCorrect) {
                credEmailEt.setText("");
                Toast.makeText(getApplicationContext(), "Email is incorrect " + email, Toast.LENGTH_SHORT).show();
            } else {
                credPanNoEt.setText("");
                Toast.makeText(getApplicationContext(), "pan is incorrect " + pan, Toast.LENGTH_SHORT).show();
            }
        }else if (name.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Name", Toast.LENGTH_SHORT).show();
        }else if (shop.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Shop Name", Toast.LENGTH_SHORT).show();
        }else if (locality.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Locality", Toast.LENGTH_SHORT).show();
        }else if (pincode.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Pin Code", Toast.LENGTH_SHORT).show();
        }else if (state.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter State", Toast.LENGTH_SHORT).show();
        }else if (mobile.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Mobile", Toast.LENGTH_SHORT).show();
        }else if (email.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Email", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Please Enter Pan Number", Toast.LENGTH_SHORT).show();
        }
    }

    //On Cancel button Click
    public void onCancelButtonClick(View view) {
        finish();
    }

    //Check if the pan no is correct
    private boolean panCheck(String s) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    //Check if the mobile no. is correct
    private boolean mobileCheck(String s){
        Pattern pattern = Pattern.compile("(0/91)?[6-9][0-9]{9}");

        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void getPsaResponse(String session_id, String auth, String name, String shop, String locality, String pincode, String state, String mobile, String email, String pan) {

        mainViewModel.getPsaRegResponse(session_id, auth, name, shop, locality, pincode, state, mobile, email, pan).observe(PanActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s.equals("Success")) {
                    credentialFormlayout.setVisibility(View.GONE);
                    credentialPageLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    credNameEt.setText("");
                    credNameEt.setHint("Full Name");
                    credShopNameEt.setText("");
                    credShopNameEt.setHint("Shop Name");
                    credLocalityEt.setText("");
                    credLocalityEt.setHint("Locality");
                    credPincodeEt.setHint("Pin code");
                    credPincodeEt.setText("");
                    credStateEt.setText("");
                    credStateEt.setHint("State");
                    credMobileEt.setHint("Mobile Number");
                    credMobileEt.setText("");
                    credEmailEt.setText("");
                    credEmailEt.setHint("Email Address");
                    credPanNoEt.setHint("Pan Number");
                    credPanNoEt.setText("");
                }
            }
        });
    }

    public String getSession_id() {
        return session_id;
    }

    public String getAuth() {
        return auth;
    }
}
