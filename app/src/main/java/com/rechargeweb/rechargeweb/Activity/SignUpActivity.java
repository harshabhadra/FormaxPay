package com.rechargeweb.rechargeweb.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rechargeweb.rechargeweb.Adapters.ReportPagerAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.SignUpLogIn.LogInFragment;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.SignUpLogIn.SignUpFragment;
import com.rechargeweb.rechargeweb.ZoomOutPageTransformer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    ReportPagerAdapter reportPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Keys keys = new Keys();
        String auth = keys.apiKey();
        Log.e(TAG,"api key from c ++ is : " + auth);
        //Getting Intent
        Intent intent = getIntent();
        if (intent.hasExtra("logout")){
            Log.e("SignUpActivity","has logout clearing data");
            SharedPreferences.Editor editor = getSharedPreferences(Constants.SAVE_ID_PASS,MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
        }
        //Initializing tabLayout and viewPager
        viewPager = findViewById(R.id.sign_up_viewPager);
        //Initializing reportPagerAdapter
        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        reportPagerAdapter.addFraqment(new LogInFragment(),"",0);
        reportPagerAdapter.addFraqment(new SignUpFragment(),"",1);
        reportPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(reportPagerAdapter);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

    public void onLogInTextClick(View view) {
        viewPager.setCurrentItem(0,true);
    }

    public void onCreateAccountTextClick(View view) {
        viewPager.setCurrentItem(1,true);
    }
}

