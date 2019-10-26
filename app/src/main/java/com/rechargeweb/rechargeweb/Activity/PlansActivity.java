package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.rechargeweb.rechargeweb.Adapters.PlanPagerAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.PlanDetails;
import com.rechargeweb.rechargeweb.PlanFragment.DataFragment;
import com.rechargeweb.rechargeweb.PlanFragment.FttFragment;
import com.rechargeweb.rechargeweb.PlanFragment.RmgFragment;
import com.rechargeweb.rechargeweb.PlanFragment.SplFragment;
import com.rechargeweb.rechargeweb.PlanFragment.TupFragment;
import com.rechargeweb.rechargeweb.R;

public class PlansActivity extends AppCompatActivity implements DataFragment.OnDataItemClickListener, SplFragment.OnSplItemClickListener
        ,FttFragment.OnFttItemClickListener, TupFragment.OnTopUpItemClickListener, RmgFragment.OnRmgItemClickListener {

    String circleId, optId;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        ViewPager viewPager = findViewById(R.id.view_pager);
        PlanPagerAdapter planPagerAdapter = new PlanPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        viewPager.setAdapter(planPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager,true);

        //Getting intent
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.CIRCLE_ID)){
            circleId = intent.getStringExtra(Constants.CIRCLE_ID);
            optId = intent.getStringExtra(Constants.OPT_ID);
            id = intent.getStringExtra(Constants.SESSION_ID);
        }
    }

    public String getCircleId() {
        return circleId;
    }

    public String getOptId() {
        return optId;
    }

    @Override
    public void onDataItemClick(PlanDetails planDetails) {

        Intent intent = new Intent(PlansActivity.this,RechargeActivity.class);
        intent.putExtra(Constants.PLAN_DETAILS,planDetails);
        intent.putExtra(Constants.RECHARGE,"recharge");
        intent.putExtra(Constants.MOBILE,"Mobile");
        intent.putExtra(Constants.SESSION_ID,id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSplItemClick(PlanDetails details) {

        Intent intent = new Intent(PlansActivity.this,RechargeActivity.class);
        intent.putExtra(Constants.PLAN_DETAILS,details);
        intent.putExtra(Constants.RECHARGE,"recharge");
        intent.putExtra(Constants.MOBILE,"Mobile");
        intent.putExtra(Constants.SESSION_ID,id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFttItemClick(PlanDetails details) {
        Intent intent = new Intent(PlansActivity.this,RechargeActivity.class);
        intent.putExtra(Constants.PLAN_DETAILS,details);
        intent.putExtra(Constants.RECHARGE,"recharge");
        intent.putExtra(Constants.MOBILE,"Mobile");
        intent.putExtra(Constants.SESSION_ID,id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTopUpItemCick(PlanDetails details) {
        Intent intent = new Intent(PlansActivity.this,RechargeActivity.class);
        intent.putExtra(Constants.PLAN_DETAILS,details);
        intent.putExtra(Constants.RECHARGE,"recharge");
        intent.putExtra(Constants.MOBILE,"Mobile");
        intent.putExtra(Constants.SESSION_ID,id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRmgItemClick(PlanDetails details) {
        Intent intent = new Intent(PlansActivity.this,RechargeActivity.class);
        intent.putExtra(Constants.PLAN_DETAILS,details);
        intent.putExtra(Constants.RECHARGE,"recharge");
        intent.putExtra(Constants.MOBILE,"Mobile");
        intent.putExtra(Constants.SESSION_ID,id);
        startActivity(intent);
        finish();
    }
}
