package com.rechargeweb.rechargeweb.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rechargeweb.rechargeweb.Adapters.CouponReportAdapter;
import com.rechargeweb.rechargeweb.Adapters.DetailsAdapter;
import com.rechargeweb.rechargeweb.Adapters.PassbookAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.AllReportViewModel;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.CouponReport;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.Model.RechargeDetails;
import com.rechargeweb.rechargeweb.R;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class AllReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        DetailsAdapter.OnDetailsItemClickListener, PassbookAdapter.OnPassbookItemClickListener {

    RecyclerView recyclerView;
    AllReportViewModel allReportViewModel;
    String auth;
    String id;
    String itemName;

    SwipeRefreshLayout refreshLayout;
    LinearLayout fromToLayout;
    ProgressBar loading;
    TextView fromDate;
    TextView toDate;
    TextView noRecordText;

    SimpleDateFormat simpleDateFormat;

    private boolean isFrom, isDate;
    String fromString;
    String toString;
    SimpleDateFormat dateFormat;

    ImageView fromToIv;

    int yy, mm, dd;

    //Adapters
    PassbookAdapter passbookAdapter;
    CouponReportAdapter couponReportAdapter;

    private static final String TAG = AllReportActivity.class.getSimpleName();

    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_report);

        //Initializing auth key
        auth = getResources().getString(R.string.auth_key);

        //Initializing ViewModel class
        allReportViewModel = ViewModelProviders.of(this).get(AllReportViewModel.class);

        //Initializing progressbar
        loading = findViewById(R.id.progressBar);

        fromToLayout = findViewById(R.id.from_to_layoput);
        //Initializing swipe refresh layout
        refreshLayout = findViewById(R.id.swipe_refresh_layout_all_report);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));

        fromDate = findViewById(R.id.from_date);
        toDate = findViewById(R.id.to_date);
        fromToIv = findViewById(R.id.from_to_iv);
        //Getting intent
        Intent intent = getIntent();

        noRecordText = findViewById(R.id.no_record_text_recharge);

        //Getting today's date
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        String[] part1 = date.split("/");
        yy = Integer.parseInt(part1[0]);
        mm = Integer.parseInt(part1[1]) - 1;
        dd = Integer.parseInt(part1[2]);

        //Setting up the activity according to intent
        if (intent.hasExtra(Constants.REPORT)) {

            //Setting up the recyclerView
            recyclerView = findViewById(R.id.report_recycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //Getting the session id from intent
            id = intent.getStringExtra(Constants.SESSION_ID);

            Items items = intent.getParcelableExtra(Constants.REPORT);
            if (items != null) {
                itemName = items.getName();

                //Set the title in the action bar
                setTitle(itemName + " Report");
                switch (itemName) {
                    case "Recharge":

                        //Make recyclerView visible
                        recyclerView.setVisibility(View.VISIBLE);
                        //Get the recharge report
                        getMobileRechargeReport();
                        break;
                    case "Credit":
                        //Get credit report
                        passbookAdapter = new PassbookAdapter(this, AllReportActivity.this);
                        recyclerView.setVisibility(View.VISIBLE);
                        getCreditReport();

                        break;
                    case "Debit":

                        //Get Debit report
                        passbookAdapter = new PassbookAdapter(this, AllReportActivity.this);
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.e(TAG, "Getting Debit report");
                        getDebitSummary();
                        break;
                    case "Coupon":
                        recyclerView.setVisibility(View.VISIBLE);
                        getCouponTransferReport();
                        break;
                    default:
                        Log.e(TAG, "Not recharge report");
                        loading.setVisibility(View.GONE);
                        fromDate.setVisibility(View.GONE);
                        toDate.setVisibility(View.GONE);
                        fromToIv.setVisibility(View.GONE);
                        break;
                }
            }
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    fromToLayout.setVisibility(View.GONE);
                } else {
                    fromToLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //Refresh the page on swipe
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                noRecordText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDate) {
                            switch (itemName) {
                                case "Recharge":
                                    //Get the recharge report
                                    getMobileRechargeReport();
                                    break;
                                case "Credit":
                                    //Get credit report
                                    getCreditReport();

                                    break;
                                case "Debit":
                                    //Get Debit report
                                    getDebitSummary();
                                    break;
                                case "Coupon Transfer":
                                    getCouponTransferReport();
                                    break;
                            }
                        } else {
                            switch (itemName) {
                                case "Recharge":
                                    getRechargeListByDate(fromString, toString);
                                    break;
                                case "Credit":
                                    getCreditReportByDate(fromString, toString);
                                    break;
                                case "Debit":
                                    getDebitSummaryByDate(fromString, toString);
                                    break;
                                case "Coupon":
                                    getCouponTransferReportByDate(fromString, toString);
                                    break;
                            }

                        }

                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        //On from date click
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFrom = true;
                showDate(yy, mm, dd, R.style.DatePickerSpinner);
            }
        });

        //On to date click
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = false;
                if (fromString != null) {
                    showDate(yy, mm, dd, R.style.DatePickerSpinner);
                } else {
                    Toast.makeText(getApplicationContext(), "Select From Date", Toast.LENGTH_SHORT).show();
                }
            }
        });
        runLayoutAnimation(recyclerView);
    }

    //Setting up the date picker
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(AllReportActivity.this)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    //Get recharge list
    private void getMobileRechargeReport() {
        Log.e(TAG, "Getting mobile recharge report");
        loading.setVisibility(View.VISIBLE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getRechargeList(id, auth).observe(this, new Observer<List<RechargeDetails>>() {
            @Override
            public void onChanged(List<RechargeDetails> rechargeDetails) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (rechargeDetails != null) {
                    Log.e(TAG, "recharge details is not null ");
                    for (int i = 0; i < rechargeDetails.size(); i++) {
                        RechargeDetails details = rechargeDetails.get(i);
                        if (details.getAmount().isEmpty()) {
                            Log.e(TAG, details.getNumber());
                            loading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(details.getApi_response());
                        } else {
                            loading.setVisibility(View.GONE);
                            Log.e(TAG, "Details list is full");
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            detailsAdapter = new DetailsAdapter(AllReportActivity.this, AllReportActivity.this, rechargeDetails);
                            recyclerView.setAdapter(detailsAdapter);
                        }
                    }

                } else {
                    Log.e(TAG, "Details list is empty");
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    //Get recharge list by date
    private void getRechargeListByDate(String fromString, String toString) {
        noRecordText.setVisibility(View.GONE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getRechargeListByDate(id, auth, fromString, toString).observe(this, new Observer<List<RechargeDetails>>() {
            @Override
            public void onChanged(List<RechargeDetails> rechargeDetails) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (rechargeDetails != null) {
                    for (int i = 0; i < rechargeDetails.size(); i++) {
                        RechargeDetails details = rechargeDetails.get(i);
                        if (details.getAmount() == null || details.getAmount().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(details.getApi_response());
                        } else {
                            loading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecordText.setVisibility(View.GONE);
                            Log.e(TAG, "Details list by selectedDate is full");
                            detailsAdapter = new DetailsAdapter(AllReportActivity.this, AllReportActivity.this, rechargeDetails);
                            recyclerView.setAdapter(detailsAdapter);
                        }
                    }

                } else {
                    Log.e(TAG, "Details list by selectedDate is empty");
                }
            }
        });
    }

    //Get credit list
    private void getCreditReport() {
        Log.e(TAG, "Getting credit report");

        loading.setVisibility(View.VISIBLE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getCreditList(id, auth).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {
                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (passbookList != null) {
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < passbookList.size(); i++) {
                        Passbook passbook = passbookList.get(i);
                        if (passbook.getTransaction_id().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(passbook.getNarration());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(passbookAdapter);
                            passbookAdapter.setPassbookListt(passbookList);
                            Log.e(TAG, "Credit list is full");
                        }
                    }
                } else {
                    Log.e(TAG, "Credit list is empty");
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    //Get Debit report
    private void getDebitSummary() {
        loading.setVisibility(View.VISIBLE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getDebitList(id, auth).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                loading.setVisibility(View.GONE);
                if (passbookList != null) {
                    Log.e(TAG, "Debit list is full");
                    for (int i = 0; i < passbookList.size(); i++) {
                        Passbook passbook = passbookList.get(i);
                        if (passbook.getTransaction_id().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(passbook.getNarration());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(passbookAdapter);
                            passbookAdapter.setPassbookListt(passbookList);

                        }
                    }
                } else {
                    Log.e(TAG, "Debit list is empty");
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    //Get credit report by date
    private void getCreditReportByDate(String fromString, String toString) {

        noRecordText.setVisibility(View.GONE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getCreditListByDate(id, auth, fromString, toString).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (passbookList != null) {
                    Log.e(TAG, "Credit list is full");
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < passbookList.size(); i++) {
                        if (passbookList.get(i).getTransaction_id().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(passbookList.get(i).getNarration());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(passbookAdapter);
                            passbookAdapter.setPassbookListt(passbookList);
                        }
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    Log.e(TAG, "Credit list is empty");
                }
            }
        });
    }

    //Get Debit summary by date
    private void getDebitSummaryByDate(String fromString, String toString) {

        noRecordText.setVisibility(View.GONE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getDebitListByDate(id, auth, fromString, toString).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {
                fromDate.setEnabled(true);
                toDate.setEnabled(true);

                if (passbookList != null) {
                    Log.e(TAG, "Debit list is full");
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < passbookList.size(); i++) {
                        if (passbookList.get(i).getTransaction_id().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(passbookList.get(i).getNarration());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(passbookAdapter);
                            passbookAdapter.setPassbookListt(passbookList);
                        }
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    Log.e(TAG, "Debit list is empty");
                }
            }
        });
    }

    //Get coupon transfer report by date
    private void getCouponTransferReportByDate(String from, String to) {

        Log.e(TAG,"Getting coupon report by date");
        noRecordText.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getCouponReportListByDate(id, auth, from, to).observe(this, new Observer<List<CouponReport>>() {
            @Override
            public void onChanged(List<CouponReport> couponReports) {
                loading.setVisibility(View.GONE);
                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (couponReports != null) {
                    Log.e(TAG, "Coupon report is full");
                    for (int i = 0; i < couponReports.size(); i++) {
                        if ((couponReports.get(i).getPsaid() == null) || couponReports.get(i).getPsaid().isEmpty()) {
                            Log.e(TAG, couponReports.get(i).getRemark());
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(couponReports.get(i).getRemark());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            couponReportAdapter = new CouponReportAdapter(AllReportActivity.this, couponReports);
                            recyclerView.setAdapter(couponReportAdapter);
                        }
                    }
                } else {
                    Log.e(TAG, "coupon report is empty");
                }
            }
        });
    }

    //Get coupon transfer report
    private void getCouponTransferReport() {

        loading.setVisibility(View.VISIBLE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        allReportViewModel.getCouponReportList(id, auth).observe(this, new Observer<List<CouponReport>>() {
            @Override
            public void onChanged(List<CouponReport> couponReports) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (couponReports != null) {
                    Log.e(TAG, "Coupon report is full");
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < couponReports.size(); i++) {
                        if (couponReports.get(i).getPsaid() == null || couponReports.get(i).getPsaid().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecordText.setVisibility(View.VISIBLE);
                            noRecordText.setText(couponReports.get(i).getRemark());
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            couponReportAdapter = new CouponReportAdapter(AllReportActivity.this, couponReports);
                            recyclerView.setAdapter(couponReportAdapter);
                        }
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    Log.e(TAG, "coupon report is empty");
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        isDate = true;
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (isFrom) {
            fromString = simpleDateFormat.format(calendar.getTime());
            fromDate.setText(fromString);
            if (toString != null && !toString.equals(getResources().getString(R.string.to_date))) {
                loading.setVisibility(View.VISIBLE);
                if (itemName.equals("Recharge")) {
                    getRechargeListByDate(fromString, toString);
                } else if (itemName.equals("Credit")) {
                    getCreditReportByDate(fromString, toString);
                } else if (itemName.equals("Debit")) {
                    getDebitSummaryByDate(fromString, toString);
                } else if (itemName.equals("Coupon")) {
                    getCouponTransferReportByDate(fromString, toString);
                }
            }
        } else {
            toString = simpleDateFormat.format(calendar.getTime());
            toDate.setText(toString);
            loading.setVisibility(View.VISIBLE);
            if (itemName.equals("Recharge")) {
                getRechargeListByDate(fromString, toString);
            } else if (itemName.equals("Credit")) {
                getCreditReportByDate(fromString, toString);
            } else if (itemName.equals("Debit")) {
                getDebitSummaryByDate(fromString, toString);
            } else if (itemName.equals("Coupon")) {
                getCouponTransferReportByDate(fromString, toString);
            }

        }
    }

    @Override
    public void onDetailsItemClick(int position) {

        RechargeDetails details = detailsAdapter.getDetailsItem(position);
        Toast.makeText(getApplicationContext(), details.getOperator_name(), Toast.LENGTH_SHORT).show();
        createRechargeDetailsDialog(details);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(AllReportActivity.this, R.style.CustomDialog);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.walletDialog);
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

    @Override
    public void onPassBookItemClick(int position) {

        Passbook passbook = passbookAdapter.getPassBook(position);
        createPassBookDialog(passbook);
    }

    private void runLayoutAnimation(RecyclerView recyclerView){

        Context context = recyclerView.getContext();

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.scheduleLayoutAnimation();
    }
}
