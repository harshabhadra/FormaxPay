package com.rechargeweb.rechargeweb.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.rechargeweb.rechargeweb.IntroFragments.IntorAepsFragment;
import com.rechargeweb.rechargeweb.IntroFragments.IntroDmtFragment;
import com.rechargeweb.rechargeweb.IntroFragments.IntroHomeFragment;
import com.rechargeweb.rechargeweb.IntroFragments.IntroMobileFragment;
import com.rechargeweb.rechargeweb.IntroFragments.IntroPanFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new IntroHomeFragment());
        addSlide(new IntroMobileFragment());
        addSlide(new IntroPanFragment());
        addSlide(new IntroDmtFragment());
        addSlide(new IntorAepsFragment());
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
