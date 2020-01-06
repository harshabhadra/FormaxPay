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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Activity.ReportActivity;
import com.rechargeweb.rechargeweb.Adapters.PassbookAdapter;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.PassBookViewModel;
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
public class PassbookFragment extends Fragment implements PassbookAdapter.OnPassbookItemClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = PassbookFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private String authKey;
    private PassBookViewModel passBookViewModel;
    PassbookAdapter passbookAdapter;
    private ProgressBar loading;
    private TextView noRecordText;
    private String id;
    private ImageView fromImageView, toImageView;
    private TextView fromTextView, toTextView;

    private SimpleDateFormat simpleDateFormat;
    private String fromString, toString;
    private int dd, mm, yyyy;
    private boolean isFromDate, isTodate;
    private AlertDialog alertDialog;
    private OnPassBookItemClickListener passBookItemClickListener;

    public PassbookFragment() {
        // Required empty public constructor
    }

    public interface OnPassBookItemClickListener{

        void onPassbookClick(Passbook passbook);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing authKey
        authKey = new Keys().apiKey();

        //Initializing ViewModel Class
        passBookViewModel = ViewModelProviders.of(this).get(PassBookViewModel.class);

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
        passbookAdapter = new PassbookAdapter(getContext(), PassbookFragment.this);
        recyclerView.setAdapter(passbookAdapter);

        if (id != null) {
            getPassbookDetails();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        passBookItemClickListener = (OnPassBookItemClickListener)context;
    }

    @Override
    public void onPassBookItemClick(int position) {

        Passbook passbook = passbookAdapter.getPassBook(position);
        passBookItemClickListener.onPassbookClick(passbook);
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
                alertDialog = createLoadingDialog(getContext());
                alertDialog.show();
                recyclerView.setVisibility(View.INVISIBLE);
                noRecordText.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPassBookDetailsByDate(fromString, toString);
                    }
                },2000);

            }
        } else {
            toString = simpleDateFormat.format(calendar.getTime());
            toTextView.setText(toString);
            if (!fromString.isEmpty() && !toString.isEmpty()) {
                alertDialog = createLoadingDialog(getContext());
                alertDialog.show();
                recyclerView.setVisibility(View.INVISIBLE);
                noRecordText.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPassBookDetailsByDate(fromString, toString);
                    }
                },2000);

            }
        }
    }

    //Get List of Passbook details
    private void getPassbookDetails() {
        fromImageView.setEnabled(false);
        toImageView.setEnabled(false);
        passBookViewModel.getPassBookDetails(id, authKey).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbooks) {
                fromImageView.setEnabled(true);
                toImageView.setEnabled(true);
                loading.setVisibility(View.GONE);
                if (passbooks != null) {
                    for (int i = 0; i < passbooks.size(); i++) {
                        if (passbooks.get(i).getTransaction_id() == null || passbooks.get(i).getTransaction_id().isEmpty()) {
                            noRecordText.setText(passbooks.get(i).getNarration());
                            noRecordText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Log.e(TAG, "Passbook list is full");
                            passbookAdapter.setPassbookListt(passbooks);
                        }
                    }

                } else {
                    Log.e(TAG, "Passbook list is empty");
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getPassBookDetailsByDate(String fromString, String toString) {
        fromImageView.setEnabled(false);
        toImageView.setEnabled(false);
        passBookViewModel.getPassbookDetailsByDate(id, authKey, fromString, toString).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {

                fromImageView.setEnabled(true);
                toImageView.setEnabled(true);
                alertDialog.dismiss();
                if (passbookList != null) {
                    for (int i = 0; i < passbookList.size(); i++) {
                        if (passbookList.get(i).getTransaction_id().isEmpty()) {
                            noRecordText.setText(passbookList.get(i).getNarration());
                            noRecordText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            passbookAdapter.setPassbookListt(passbookList);
                        }
                    }

                } else {
                    noRecordText.setText("No record Found");
                    noRecordText.setVisibility(View.VISIBLE);
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
