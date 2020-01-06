package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Support;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.LayoutSupportBinding;


public class SupportActivity extends AppCompatActivity {

    boolean techCallButton, techMailButton;
    boolean salesCallButton, salesMailButton;
    boolean billCallButton, billMailButton;

    private static final String TAG = SupportActivity.class.getSimpleName();

    String auth_key;

    private String saleCall,salesMail, techCall, techMail, billCall, billMail,about, address;
    MainViewModel mainViewModel;

    LayoutSupportBinding layoutSupportBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_support);

        //Set all boolean values to false
        techCallButton = false;
        techMailButton = false;
        salesCallButton = false;
        salesMailButton = false;
        billCallButton = false;
        billMailButton = false;

        //Auth key
        auth_key = new Keys().apiKey();

        //Initializing MainViewModel class
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Initializing dataBinding
        layoutSupportBinding = DataBindingUtil.setContentView(SupportActivity.this,R.layout.layout_support);

        //Show or hide tech call now button
        layoutSupportBinding.technicalCallNowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!techCallButton) {
                    layoutSupportBinding.techCallButton.setVisibility(View.VISIBLE);
                    techCallButton = true;
                } else {
                    layoutSupportBinding.techCallButton.setVisibility(View.GONE);
                    techCallButton = false;
                }
            }
        });

        //Show or hide tech mail now button
        layoutSupportBinding.technicalMailNowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!techMailButton) {
                    layoutSupportBinding.techMailButton.setVisibility(View.VISIBLE);
                    techMailButton = true;
                } else {
                    layoutSupportBinding.techMailButton.setVisibility(View.GONE);
                    techMailButton = false;
                }
            }
        });

        //Show or hide Sales call now button
        layoutSupportBinding.salesCallTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!salesCallButton) {
                    layoutSupportBinding.salesCallButton.setVisibility(View.VISIBLE);
                    salesCallButton = true;
                } else {
                    layoutSupportBinding.salesCallButton.setVisibility(View.GONE);
                    salesCallButton = false;
                }
            }
        });

        //Show or hide sales mail now button
        layoutSupportBinding.salesEmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!salesMailButton) {
                    layoutSupportBinding.salesMailButton.setVisibility(View.VISIBLE);
                    salesMailButton = true;
                } else {
                    layoutSupportBinding.salesMailButton.setVisibility(View.GONE);
                    salesMailButton = false;
                }
            }
        });

        //Show or hide bill call button
        layoutSupportBinding.billCallTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!billCallButton){
                    layoutSupportBinding.billCallButton.setVisibility(View.VISIBLE);
                    billCallButton = true;
                }else {
                    layoutSupportBinding.billCallButton.setVisibility(View.GONE);
                    billCallButton = false;
                }
            }
        });

        //Show or hide bill mail button
        layoutSupportBinding.billMailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!billMailButton){
                    layoutSupportBinding.billMailButton.setVisibility(View.VISIBLE);
                    billMailButton = true;
                }else {
                    layoutSupportBinding.billMailButton.setVisibility(View.GONE);
                    billMailButton = false;
                }
            }
        });


        //Open dialer
        layoutSupportBinding.techCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + techCall));
                startActivity(intent);
            }
        });

        layoutSupportBinding.salesCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + saleCall));
                startActivity(intent);
            }
        });

        layoutSupportBinding.billCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + billCall));
                startActivity(intent);
            }
        });

        //Open default mail
        layoutSupportBinding.techMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                startActivity(intent);
            }
        });

        layoutSupportBinding.salesMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                startActivity(intent);
            }
        });

        layoutSupportBinding.billMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                startActivity(intent);
            }
        });

        //Get response in on create
        getSupportDetails();
    }

    //Getting support information
    private void getSupportDetails(){

        View layout = getLayoutInflater().inflate(R.layout.loading_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.show();

        mainViewModel.getSupportDetails(auth_key).observe(this, new Observer<Support>() {
            @Override
            public void onChanged(Support support) {

                dialog.dismiss();
                if (support != null){

                    if (support.getEmail() == null){
                        Log.e(TAG, "Error getting response");
                    }else {
                        Log.e(TAG, "Support list is full");
                        saleCall = support.getMobileOne();
                        salesMail = support.getEmail();

                        techCall = support.getMobileTwo();
                        techMail = support.getEmailTwo();

                        billCall = support.getMobileThree();
                        billMail = support.getBillEmail();

                        about = support.getAboutUd();
                        address = support.getAddress();

                        layoutSupportBinding.salesCallTv.setText(saleCall);
                        layoutSupportBinding.salesEmailTv.setText(salesMail);

                        layoutSupportBinding.technicalCallNowTv.setText(techCall);
                        layoutSupportBinding.technicalMailNowTv.setText(techMail);

                        layoutSupportBinding.billCallTv.setText(billCall);
                        layoutSupportBinding.billMailTv.setText(billMail);

                        layoutSupportBinding.supportAddTv.setText(address);
                    }
                }else {
                    Log.e(TAG,"support is empty");
                }
            }
        });
    }

}
