package com.rechargeweb.rechargeweb.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rechargeweb.rechargeweb.BottomSheetFrag.BankNameBottomFragment;
import com.rechargeweb.rechargeweb.BottomSheetFrag.PaymentModeBottomSheet;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Bank;
import com.rechargeweb.rechargeweb.R;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FundRequestActivity extends AppCompatActivity implements BankNameBottomFragment.OnClickListener,
        PaymentModeBottomSheet.OnPaymentClickListener, DatePickerDialog.OnDateSetListener {

    EditText amoutEdit, transactionIdEdit, accountEdit;

    TextView selectBankEdit, dateEdit,paymentMethodEdit;
    TextView chooseWallet;
    ProgressBar fundProgress;

    RadioButton wallletOneRadioButton, walletTwoRadioButton;

    Button submitButton;

    String bankName, account, amount, paymentMethod, transId;

    String wallet;

    String selectedDate;

    SimpleDateFormat simpleDateFormat;


    String session_id;

    String auth_key;

    int yy, mm, dd;

    MainViewModel mainViewModel;

    private static final String TAG = FundRequestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request);

        amoutEdit = findViewById(R.id.fund_amount);
        selectBankEdit = findViewById(R.id.fund_select_bank);
        paymentMethodEdit = findViewById(R.id.fund_payment_mode);
        dateEdit = findViewById(R.id.fund_date);
        transactionIdEdit = findViewById(R.id.fund_transaction_id);
        accountEdit = findViewById(R.id.fund_account);
        submitButton = findViewById(R.id.fund_button);
        fundProgress = findViewById(R.id.fund_progress_bar);

        chooseWallet = findViewById(R.id.choose_wallet);

        Intent intent = getIntent();

        session_id = intent.getStringExtra(Constants.SESSION_ID);
        auth_key = getResources().getString(R.string.auth_key);
        wallet = "wallet_1";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        final String date = dateFormat.format(calendar.getTime());

        String[] part1 = date.split("/");
        yy = Integer.parseInt(part1[0]);
        mm = Integer.parseInt(part1[1]) - 1;
        dd = Integer.parseInt(part1[2]);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        selectBankEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBankListBottomSheet();
            }
        });

        //Open payment mode list
        paymentMethodEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentModeBottomSheet();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(submitButton.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                amount = amoutEdit.getText().toString();
                transId = transactionIdEdit.getText().toString();
                if (!(amount.isEmpty()) && bankName != null && paymentMethod != null && selectedDate != null && !(transId.isEmpty()) && wallet != null) {

                    View laout = getLayoutInflater().inflate(R.layout.loading_dialog,null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(FundRequestActivity.this);
                    builder.setView(laout);
                    builder.setCancelable(false);
                    final AlertDialog updateDialog = builder.create();
                    updateDialog.show();

                    mainViewModel.getFundResponse(session_id, auth_key, amount, bankName, paymentMethod, selectedDate, transId, wallet).observe(FundRequestActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {

                            updateDialog.dismiss();
                            Intent intent1 = new Intent(FundRequestActivity.this,FundRequestActivity.class);
                            intent1.putExtra(Constants.SESSION_ID,session_id);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (amount.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Amount", Toast.LENGTH_LONG).show();
                } else if (bankName == null) {
                    Toast.makeText(getApplicationContext(), "Please Enter Bank Name", Toast.LENGTH_LONG).show();
                } else if (paymentMethod == null) {
                    Toast.makeText(getApplicationContext(), "Please Select Payment Method", Toast.LENGTH_LONG).show();
                } else if (selectedDate == null) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                } else if (transId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Transaction Id", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Wallet", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Open selectedDate spinner
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(yy, mm, dd, R.style.DatePickerSpinner);
            }
        });

        chooseWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] wallets = {"wallet_1", "wallet_2"};
                chooseWallet.setText(wallets[0]);
                wallet = wallets[0];
                final int position = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(FundRequestActivity.this);
                builder.setTitle("Choose Wallet");
                builder.setSingleChoiceItems(wallets, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == position){
                            wallet = wallets[0];
                            chooseWallet.setText(wallets[0]);
                            dialog.dismiss();
                        }else {
                            wallet = wallets[1];
                            chooseWallet.setText(wallets[1]);
                            dialog.dismiss();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    //Show selectedDate Spinner
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(FundRequestActivity.this)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }


    //Show bank bottom sheet
    private void showBankListBottomSheet() {
        BankNameBottomFragment bankNameBottomFragment = new BankNameBottomFragment();
        bankNameBottomFragment.show(getSupportFragmentManager(), bankNameBottomFragment.getTag());
    }

    //Show payment mode bottom sheet
    private void showPaymentModeBottomSheet() {
        PaymentModeBottomSheet paymentModeBottomSheet = new PaymentModeBottomSheet();
        paymentModeBottomSheet.show(getSupportFragmentManager(), paymentModeBottomSheet.getTag());
    }

    @Override
    public void onItemClick(Bank bank) {

        //Set Bank details
        bankName = bank.getBankName() + " " + "(" + bank.getIfscCode() + ")";
        account = bank.getAccountHolder() + " " + "(" + bank.getAccountNumber() + ")";
        selectBankEdit.setText(bankName);
        accountEdit.setVisibility(View.VISIBLE);
        accountEdit.setText(account);
    }

    @Override
    public void onPaymentClick(String string) {

        //Set payment method
        paymentMethod = string;
        paymentMethodEdit.setText(paymentMethod);
    }

    //Set selectedDate to the edit text when user choose selectedDate from the spinner
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        selectedDate = simpleDateFormat.format(calendar.getTime());

        dateEdit.setText(selectedDate);
    }

}
