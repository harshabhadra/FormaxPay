package com.rechargeweb.rechargeweb.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.Model.AepsLogIn;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.UploadKycFragment;
import com.rechargeweb.rechargeweb.ViewModels.AepsViewModel;
import com.rechargeweb.rechargeweb.YblAepsFragment;

public class AepsActivity extends AppCompatActivity {

    private String session_id, user_id, auth_key;
    private AepsViewModel aepsViewModel;

    private FragmentTransaction fragmentTransaction;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);

        //Getting values from Intent
        Intent intent = getIntent();
        session_id = intent.getStringExtra(Constants.SESSION_ID);
        user_id = intent.getStringExtra(Constants.USER_ID);

        //Initializing Auth Key
        auth_key = new Keys().apiKey();
        String service_type = "YBL_AEPS";

       fragmentTransaction = getSupportFragmentManager().beginTransaction();

       //Initializing AepsViewModel
        aepsViewModel = ViewModelProviders.of(this).get(AepsViewModel.class);

       //Create Loading Dialog
       alertDialog = createAlertDialog(this);

        aepsLogIn(session_id,service_type,auth_key);
    }

    //Log in aeps
    private void aepsLogIn(String session_id,String serviceType, String auth_key){
        aepsViewModel.aepsLogIn(session_id,serviceType,auth_key).observe(this, new Observer<AepsLogIn>() {
            @Override
            public void onChanged(AepsLogIn aepsLogIn) {

                alertDialog.dismiss();
                if (aepsLogIn != null){
                    if (aepsLogIn.getStatus().equals("Approved") || aepsLogIn.getStatus().equals("APPROVED")){

                        YblAepsFragment yblAepsFragment = new YblAepsFragment();
                        fragmentTransaction.replace(R.id.aeps_container,yblAepsFragment).commit();
                    }else {
                        UploadKycFragment uploadKycFragment = new UploadKycFragment(aepsLogIn);
                        fragmentTransaction.replace(R.id.aeps_container,uploadKycFragment).commit();
                    }
                }
            }
        });
    }

    //Create Loading Dialog
    private AlertDialog createAlertDialog(Context context){

        View view = getLayoutInflater().inflate(R.layout.loading_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    public String getSession_id() {
        return session_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
