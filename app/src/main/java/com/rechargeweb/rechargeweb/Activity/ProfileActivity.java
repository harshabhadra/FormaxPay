package com.rechargeweb.rechargeweb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Profile;
import com.rechargeweb.rechargeweb.R;


public class ProfileActivity extends AppCompatActivity {

    TextView profileAddress, profileMobile, profileDate, userType, panNO, gstNo, stateTv, pincodeTv,profileName,businessNameTv;
    String title;

    ImageView activeImage;

    String address, pan, gst,status;

    CardView titleLayoutCard;
    CardView cardtype;
    CardView cardAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        profileAddress = findViewById(R.id.profile_address);
        profileMobile = findViewById(R.id.profile_name);
        profileDate = findViewById(R.id.profile_date);
        userType = findViewById(R.id.profile_user_type);
        panNO = findViewById(R.id.profile_pan);
        gstNo = findViewById(R.id.profile_gst);
        stateTv = findViewById(R.id.profile_state);
        pincodeTv = findViewById(R.id.profile_pincode);
        profileName = findViewById(R.id.profile_n);
        activeImage = findViewById(R.id.active_image);
        businessNameTv = findViewById(R.id.business_name_profile);

        titleLayoutCard = findViewById(R.id.title_layout);
        cardtype = findViewById(R.id.card_type);
        cardAddress = findViewById(R.id.card_address);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        titleLayoutCard.startAnimation(animation);
        cardtype.startAnimation(animation);
        cardAddress.startAnimation(animation);

        Intent intent = getIntent();

        if (intent.hasExtra(Constants.PROFILE)) {
            Profile profile = intent.getParcelableExtra(Constants.PROFILE);
            setTitle("Profile");
            if (profile != null) {
                title = profile.getBusinessName();
                businessNameTv.setText(title);

                address = profile.getAddress();
                pan = profile.getPan_no();
                gst = profile.getGst_no();
                status = profile.getStatus();
            }
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();

            profileAddress.setText(address);
            profileMobile.setText(profile.getMobile());
            profileDate.setText(profile.getCreatedOn());
            userType.setText(profile.getUser_type());
            profileName.setText(profile.getName());
            if (status.equals("Active")) {
                activeImage.setVisibility(View.VISIBLE);
            }

            if (pan != null) {
                if (!pan.isEmpty()) {
                    panNO.setText(profile.getPan_no());
                }
            }

            if (gst != null) {
                if (!gst.isEmpty())
                    gstNo.setText(profile.getGst_no());
            }
            stateTv.setText(profile.getLocation());
            pincodeTv.setText(profile.getPincode());
        }
    }
}
