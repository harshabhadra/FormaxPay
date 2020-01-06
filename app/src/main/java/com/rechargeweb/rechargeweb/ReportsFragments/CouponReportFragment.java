package com.rechargeweb.rechargeweb.ReportsFragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Activity.ReportActivity;
import com.rechargeweb.rechargeweb.Adapters.CouponReportAdapter;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.Model.CouponReport;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.AllReportViewModel;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = CouponReportAdapter.class.getSimpleName();
    private RecyclerView recyclerView;
    private String authKey;
    private AllReportViewModel allReportViewModel;
    private CouponReportAdapter couponReportAdapter;
    private ProgressBar loading;
    private TextView noRecordText;
    private String id;
    private ImageView fromImageView, toImageView;
    private TextView fromTextView, toTextView;

    private SimpleDateFormat simpleDateFormat;
    private String fromString, toString;
    private int dd, mm, yyyy;
    private boolean isFromDate, isTodate;
     AlertDialog couponAlertDialog;

    public CouponReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing authKey
        authKey = new Keys().apiKey();

        //Initializing ViewModel Class
        allReportViewModel = ViewModelProviders.of(this).get(AllReportViewModel.class);

        ReportActivity reportActivity = (ReportActivity) getActivity();
        if (reportActivity != null) {
            id = reportActivity.getSession_id();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_report, container, false);

        //Setting up from date and to date image and text view
        fromImageView = view.findViewById(R.id.report_fromdate_iv);
        toImageView = view.findViewById(R.id.report_todate_iv);

        fromTextView = view.findViewById(R.id.from_date_tv);
        toTextView = view.findViewById(R.id.todate_tv);

        //Initializing RecyclerView
        recyclerView = view.findViewById(R.id.nav_report_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (id != null) {
            getCouponTransferReport();
        } else {
            Log.e(TAG, "Id is: " + id);
        }
        //Initializing progressbar and no record Text View
        loading = view.findViewById(R.id.report_loading_indicator);
        noRecordText = view.findViewById(R.id.no_record_text_recharge);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        toString = simpleDateFormat.format(calendar.getTime());
        fromString = toString;
        fromTextView.setText(toString);
        toTextView.setText(toString);

        //Getting seperate values of year, month and day from the String today
        String[] part1 = toString.split("/");
        yyyy = Integer.parseInt(part1[0]);
        mm = Integer.parseInt(part1[1]) - 1;
        dd = Integer.parseInt(part1[2]);

        //On From ImageView Click
        fromImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDate = true;
                isTodate = false;
                showDate(yyyy, mm, dd, R.style.DatePickerSpinner);
            }
        });

        //On To ImageView Click
        toImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTodate = true;
                isFromDate = false;
                showDate(yyyy, mm, dd, R.style.DatePickerSpinner);
            }
        });
    }

    //Setting up the date picker
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (isFromDate) {
            fromString = simpleDateFormat.format(calendar.getTime());
            fromTextView.setText(fromString);
            if (!fromString.isEmpty() && !toString.isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                noRecordText.setVisibility(View.INVISIBLE);
                getCouponTransferReportByDate(fromString,toString);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },2000);

            }
        } else {
            toString = simpleDateFormat.format(calendar.getTime());
            toTextView.setText(toString);
            if (!fromString.isEmpty() && !toString.isEmpty()){
                recyclerView.setVisibility(View.INVISIBLE);
                noRecordText.setVisibility(View.INVISIBLE);
                getCouponTransferReportByDate(fromString,toString);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },2000);
            }
        }
    }

    //Get coupon transfer report
    private void getCouponTransferReport() {
        fromImageView.setEnabled(false);
        toImageView.setEnabled(false);
        allReportViewModel.getCouponReportList(id, authKey).observe(this, new Observer<List<CouponReport>>() {
            @Override
            public void onChanged(List<CouponReport> couponReports) {

                fromImageView.setEnabled(true);
                toImageView.setEnabled(true);
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
                            couponReportAdapter = new CouponReportAdapter(getContext(), couponReports);
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

    //Get coupon transfer report by date
    private void getCouponTransferReportByDate(String from, String to) {

        loading.setVisibility(View.VISIBLE);
        noRecordText.setVisibility(View.GONE);
        fromImageView.setEnabled(false);
        toImageView.setEnabled(false);
        Log.e(TAG,"Getting coupon report by date");
        allReportViewModel.getCouponReportListByDate(id, authKey, from, to).observe(this, new Observer<List<CouponReport>>() {
            @Override
            public void onChanged(List<CouponReport> couponReports) {
                toImageView.setEnabled(true);
                fromImageView.setEnabled(true);
                loading.setVisibility(View.INVISIBLE);
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
                            couponReportAdapter = new CouponReportAdapter(getContext(), couponReports);
                            recyclerView.setAdapter(couponReportAdapter);
                        }
                    }
                } else {
                    Log.e(TAG, "coupon report is empty");
                }
            }
        });
    }

    private AlertDialog createLoadingDialog(Context context){

        View layout = getLayoutInflater().inflate(R.layout.loading_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(layout);
        return builder.create();
    }
}
