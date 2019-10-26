package com.rechargeweb.rechargeweb.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.DmtViewModel;
import com.rechargeweb.rechargeweb.databinding.ActivityDmtBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DmtActivity extends AppCompatActivity {

    DmtViewModel dmtViewModel;
    String auth;
    String session_id;

    ActivityDmtBinding dmtBinding;
    private String mobile;
    private boolean isMobileCorrect;

    private static final String TAG = DmtActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmt);

        //Get intent
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)){
            session_id = intent.getStringExtra(Constants.SESSION_ID);
        }
        dmtBinding = DataBindingUtil.setContentView(this, R.layout.activity_dmt);

        auth = getResources().getString(R.string.auth_key);

        dmtViewModel = ViewModelProviders.of(this).get(DmtViewModel.class);

        //On button Click
        dmtBinding.dmtSerachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(dmtBinding.dmtSerachButton.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                mobile = dmtBinding.numberRemmiter.getText().toString().trim();
                isMobileCorrect = mobileCheck(mobile);
                if (isMobileCorrect) {
                    dmtBinding.dmtLoading.setVisibility(View.VISIBLE);
                    dmtBinding.dmtSerachButton.setVisibility(View.GONE);
                    searchEmitterNumber(auth, mobile);
                }else {
                    dmtBinding.textInputLayout2.setError("Enter Valid Mobile Number");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Search emitter number
    private void searchEmitterNumber(final String auth, final String mobile) {
        dmtViewModel.getRemitterMessage(auth, mobile).observe(DmtActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dmtBinding.dmtLoading.setVisibility(View.GONE);
                dmtBinding.dmtSerachButton.setVisibility(View.VISIBLE);

                if (s.equals("Transaction Successful")) {
                    Intent intent = new Intent(DmtActivity.this, RemitterActivity.class);
                    intent.putExtra(Constants.REMITTER_MOBILE, mobile);
                    intent.putExtra(Constants.SESSION_ID,session_id);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(DmtActivity.this, ValidateActivity.class);
                    intent.putExtra(Constants.REMITTER_MOBILE, mobile);
                    intent.putExtra(Constants.MESSAGE, s);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Check if the mobile no. is correct
    private boolean mobileCheck(String s) {
        Pattern pattern = Pattern.compile("(0/91)?[6-9][0-9]{9}");

        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
