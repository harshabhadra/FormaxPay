package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.rechargeweb.rechargeweb.Adapters.PlanPagerAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.R;

public class PlansActivity extends AppCompatActivity {

    String circleId, optId;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

//        ViewPager viewPager = findViewById(R.id.view_pager);
//        PlanPagerAdapter planPagerAdapter = new PlanPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
//        viewPager.setAdapter(planPagerAdapter);

//        TabLayout tabLayout = findViewById(R.id.tab);
//        tabLayout.setupWithViewPager(viewPager,true);

        //Getting intent
        Intent intent = getIntent();
    }

    public String getCircleId() {
        return circleId;
    }

    public String getOptId() {
        return optId;
    }

}
