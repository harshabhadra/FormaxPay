package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.rechargeweb.rechargeweb.Adapters.ReportPagerAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.Model.RechargeDetails;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ReportsFragments.AepsReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.CouponReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.CreditReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.DebitReportFragment;
import com.rechargeweb.rechargeweb.ReportsFragments.RechargeReportFragment;

public class ReportActivity extends AppCompatActivity implements
        RechargeReportFragment.OnRecharReportItemClickListener,
        CreditReportFragment.OnCreditItemClickListner,
        DebitReportFragment.OnDebitItemClickListener{

    private ReportPagerAdapter reportPagerAdapter;
    private String session_id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)){
            session_id = intent.getStringExtra(Constants.SESSION_ID);
        }
        ViewPager viewPager = findViewById(R.id.report_viewPager);
        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        if (intent.hasExtra(Constants.REPORT)){
            Items items = intent.getParcelableExtra(Constants.REPORT);
            title = items.getName();
           switch (title){
               case "Recharge":
                   setSingleReportPager(viewPager,new RechargeReportFragment(),title);
                   break;
               case "Credit":
                   setSingleReportPager(viewPager, new CreditReportFragment(),title);
                   break;
               case "Debit":
                   setSingleReportPager(viewPager,new DebitReportFragment(),title);
                   break;
               case "Coupon":
                   setSingleReportPager(viewPager, new CouponReportFragment(),title);
                   break;
               case "AEPS":
                   setSingleReportPager(viewPager,new AepsReportFragment(),title);
                   break;
           }
        }else {
            setReportPagerAdapter(viewPager);
        }
        TabLayout tabLayout = findViewById(R.id.report_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //Set up view pager for single element
    private void setSingleReportPager(ViewPager viewPager, Fragment fragment, String title){
        reportPagerAdapter.addFraqment(fragment,title,0);
        reportPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(reportPagerAdapter);
    }
    //Set up View Pager for all
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

    @Override
    public void onRehcargeReportItmeClick(RechargeDetails details) {

        if (details != null){
            createRechargeDetailsDialog(details);
        }else {
            Toast.makeText(getApplicationContext(),"Recharge Details is empty",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreditItemClick(Passbook passbook) {

        if (passbook != null){
            createPassBookDialog(passbook);
        }else {
            Toast.makeText(getApplicationContext(),"No Details to Show", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDebitItemClick(Passbook passbook) {

        if (passbook != null){
            createPassBookDialog(passbook);
        }else {
            Toast.makeText(getApplicationContext(),"No Details to Show", Toast.LENGTH_SHORT).show();
        }
    }

    //Create dialog for recharge details
    private void createRechargeDetailsDialog(RechargeDetails details) {

        View layout = getLayoutInflater().inflate(R.layout.recharge_detail_dialog, null);

        TextView rechargeBy = layout.findViewById(R.id.details_recharge_by);
        TextView txnTv = layout.findViewById(R.id.details_txn_id);
        TextView optTxnTv = layout.findViewById(R.id.details_opt_txn_id);
        TextView balanceTv = layout.findViewById(R.id.details_balance);
        TextView responseTv = layout.findViewById(R.id.details_api_response);
        Button closeButton = layout.findViewById(R.id.detail_close);

        AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this, R.style.CustomDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        rechargeBy.setText(details.getRecharge_by());
        txnTv.setText(details.getTxn_id());
        optTxnTv.setText(details.getOpt_txn_id());
        balanceTv.setText(details.getClosing_balance());
        responseTv.setText(details.getApi_response());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //Create dialog for credit details
    private void createPassBookDialog(Passbook passbook) {

        View layout = getLayoutInflater().inflate(R.layout.passbook_dialog, null);

        TextView closebal = layout.findViewById(R.id.closing_balance_passbook);
        TextView openbal = layout.findViewById(R.id.opening_balance_passbook);
        TextView wallet = layout.findViewById(R.id.narration_passbook);
        TextView transaction = layout.findViewById(R.id.traction_passbook);
        Button button = layout.findViewById(R.id.close_passbook);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        closebal.setText(passbook.getClosing_balalnce());
        openbal.setText(passbook.getOpening_balance());
        wallet.setText(passbook.getWallet_type());
        transaction.setText(passbook.getTransaction_id());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
