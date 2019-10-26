package com.rechargeweb.rechargeweb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

import com.rechargeweb.rechargeweb.Adapters.PassbookAdapter;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
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

public class PassbookActivity extends AppCompatActivity implements PassbookAdapter.OnPassbookItemClickListener, DatePickerDialog.OnDateSetListener {

    RecyclerView passbookRecycler;
    PassbookAdapter passbookAdapter;

    PassBookViewModel passBookViewModel;

    String session_id;
    String auth;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView fromDate;
    TextView toDate;
    ImageView fromToIv;
    LinearLayout fromToLayout;

    SimpleDateFormat simpleDateFormat;

    private boolean isFrom, isDate;
    String fromString;
    String toString;
    SimpleDateFormat dateFormat;

    TextView noRecordText;

    int yy, mm, dd;

    private static final String TAG = PassbookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        fromDate = findViewById(R.id.from_date);
        toDate = findViewById(R.id.to_date);
        fromToIv = findViewById(R.id.from_to_iv);
        fromToLayout = findViewById(R.id.from_to_layoput);

        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        Log.e(TAG, date);
        String[] part1 = date.split("/");
        yy = Integer.parseInt(part1[0]);
        mm = Integer.parseInt(part1[1]) - 1;
        dd = Integer.parseInt(part1[2]);


        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)) {
            session_id = intent.getStringExtra(Constants.SESSION_ID);
        }

        auth = getResources().getString(R.string.auth_key);
        progressBar = findViewById(R.id.passbook_loading);
        progressBar.setVisibility(View.VISIBLE);

        noRecordText = findViewById(R.id.no_record_pass);
        passbookRecycler = findViewById(R.id.passbook_recycler);
        passbookRecycler.setVisibility(View.GONE);
        passbookRecycler.setHasFixedSize(true);
        passbookRecycler.setLayoutManager(new LinearLayoutManager(this));
        passbookAdapter = new PassbookAdapter(this, this);
        passbookRecycler.setAdapter(passbookAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_pass);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));


        //Hide fromTolayout on RecyclerView scroll
        passbookRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        //Refresh the layout on swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                noRecordText.setVisibility(View.GONE);
                passbookRecycler.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDate) {
                            getPassbookDetails();
                        } else {
                            getPassBookDetailsByDate(fromString, toString);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });


        //Initializing ViewModel Class
        passBookViewModel = ViewModelProviders.of(this).get(PassBookViewModel.class);

        getPassbookDetails();

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFrom = true;
                showDate(yy, mm, dd, R.style.DatePickerSpinner);
            }
        });

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
    }

    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(PassbookActivity.this)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    //Get List of Passbook details
    private void getPassbookDetails() {
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        passbookRecycler.setVisibility(View.GONE);
        passBookViewModel.getPassBookDetails(session_id, auth).observe(this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbooks) {
                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (passbooks != null) {
                    for (int i = 0; i < passbooks.size(); i++) {
                        progressBar.setVisibility(View.GONE);
                        if (passbooks.get(i).getTransaction_id() == null || passbooks.get(i).getTransaction_id().isEmpty()) {
                            noRecordText.setText(passbooks.get(i).getNarration());
                            noRecordText.setVisibility(View.VISIBLE);
                            passbookRecycler.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            passbookRecycler.setVisibility(View.VISIBLE);
                            Log.e(TAG, "Passbook list is full");
                            passbookAdapter.setPassbookListt(passbooks);
                        }
                    }

                } else {
                    Log.e(TAG, "Passbook list is empty");
                }
            }
        });
    }

    @Override
    public void onPassBookItemClick(int position) {

        Passbook passbook = passbookAdapter.getPassBook(position);
        createPassBookDialog(passbook);
    }

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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        isDate = true;
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (isFrom) {
            fromString = simpleDateFormat.format(calendar.getTime());
            fromDate.setText(fromString);

            if (toString != null && !toString.equals(getResources().getString(R.string.to_date))) {
                getPassBookDetailsByDate(fromString, toString);
                progressBar.setVisibility(View.VISIBLE);
                noRecordText.setVisibility(View.GONE);
            }
        } else {
            toString = simpleDateFormat.format(calendar.getTime());
            toDate.setText(toString);
            getPassBookDetailsByDate(fromString, toString);
            progressBar.setVisibility(View.VISIBLE);
            noRecordText.setVisibility(View.GONE);
        }
    }

    private void getPassBookDetailsByDate(String fromString, String toString) {

        passbookRecycler.setVisibility(View.GONE);
        fromDate.setEnabled(false);
        toDate.setEnabled(false);
        passBookViewModel.getPassbookDetailsByDate(session_id, auth, fromString, toString).observe(PassbookActivity.this, new Observer<List<Passbook>>() {
            @Override
            public void onChanged(List<Passbook> passbookList) {

                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                noRecordText.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                passbookRecycler.setVisibility(View.VISIBLE);
                if (passbookList != null) {
                    for (int i = 0; i < passbookList.size(); i++) {
                        if (passbookList.get(i).getTransaction_id().isEmpty()) {
                            noRecordText.setText(passbookList.get(i).getNarration());
                            noRecordText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            passbookRecycler.setVisibility(View.GONE);
                        } else {
                            noRecordText.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            passbookRecycler.setVisibility(View.VISIBLE);
                            passbookAdapter.setPassbookListt(passbookList);
                        }
                    }

                } else {
                    noRecordText.setText("No record Found");
                    noRecordText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    passbookRecycler.setVisibility(View.GONE);
                }
            }
        });
    }
}
