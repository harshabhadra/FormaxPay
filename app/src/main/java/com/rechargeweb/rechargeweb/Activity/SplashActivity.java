package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.R;

public class SplashActivity extends AppCompatActivity {

    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.FIRST_TIME_USER,MODE_PRIVATE);
        isFirstTime = sharedPreferences.getBoolean(Constants.IS_FIRST_TIEM,true);

        Handler handler = new Handler();

        Intent intent = getIntent();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isFirstTime) {
                    Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent1 = new Intent(SplashActivity.this,IntroActivity.class);
                    startActivity(intent1);
                    finish();
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.FIRST_TIME_USER,MODE_PRIVATE).edit();
                    editor.putBoolean(Constants.IS_FIRST_TIEM,true);
                    editor.apply();
                }
            }
        },3000);
    }
}
