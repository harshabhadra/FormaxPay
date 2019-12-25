package com.rechargeweb.rechargeweb.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.rechargeweb.rechargeweb.Intro_home;
import com.rechargeweb.rechargeweb.R;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new Intro_home());
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("WELCOME");
        sliderPage.setDescription("Best Recharge App For Retailers. Mobile, DTh Recharge, Buy Pan Coupon, Direct Money Transfer");
        sliderPage.setBgColor(getResources().getColor(R.color.colorAccent));
        sliderPage.setImageDrawable(R.drawable.fp_home);
        sliderPage.setTitleColor(Color.WHITE);
        sliderPage.setDescColor(Color.WHITE);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Mobile & DTH Recharge");
        sliderPage2.setDescription("Do Mobile and Dth Recharge for variety providers and earn cash ");
        sliderPage2.setBgColor(getResources().getColor(R.color.colorAccent));
        sliderPage2.setImageDrawable(R.drawable.recharge_fp);
        sliderPage2.setTitleColor(Color.WHITE);
        sliderPage2.setDescColor(Color.WHITE);
        showSkipButton(true);
        setProgressButtonEnabled(true);
        addSlide(AppIntroFragment.newInstance(sliderPage2));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
