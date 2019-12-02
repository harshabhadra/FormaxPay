package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.rechargeweb.rechargeweb.Adapters.ReportPagerAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ReportsFragments.AepsReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.CouponReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.CreditReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.DebitReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.RechargeReportFragment;

public class ReportActivity extends AppCompatActivity {

    private ReportPagerAdapter reportPagerAdapter;
    private String session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ViewPager viewPager = findViewById(R.id.report_viewPager);
        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        setReportPagerAdapter(viewPager);
        TabLayout tabLayout = findViewById(R.id.report_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)){
            session_id = intent.getStringExtra(Constants.SESSION_ID);
        }
    }

    private void setReportPagerAdapter(ViewPager viewPager){
        reportPagerAdapter.addFraqment(new RechargeReportFragment(),"Recharge Report",0);
        reportPagerAdapter.addFraqment(new CouponReportFragment(),"Coupon Report",1);
        reportPagerAdapter.addFraqment(new AepsReportFragment(),"AEPS Report",2);
        reportPagerAdapter.addFraqment(new CreditReportFragment(),"Credit Report",3);
        reportPagerAdapter.addFraqment(new DebitReportFragment(),"Debit Report",4);
        reportPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(reportPagerAdapter);
    }

    public String getSession_id() {
        return session_id;
    }
}
