package com.rechargeweb.rechargeweb.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.rechargeweb.rechargeweb.BottomSheetFrag.BottomSheetFragment;
import com.rechargeweb.rechargeweb.BottomSheetFrag.DTHSheetFragment;
import com.rechargeweb.rechargeweb.BottomSheetFrag.PostPaidSheet;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.Model.NumberDetect;
import com.rechargeweb.rechargeweb.Model.PlanDetails;
import com.rechargeweb.rechargeweb.Model.Prepaid;
import com.rechargeweb.rechargeweb.Model.Recharge;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.RechargeViewModel;
import com.squareup.picasso.Picasso;

import mehdi.sakout.fancybuttons.FancyButton;

public class RechargeActivity extends AppCompatActivity implements BottomSheetFragment.OnPrepaidListClickLietener,
        PostPaidSheet.OnPostPaidClickListener, DTHSheetFragment.OnDthSheetItemClickListener {

    String name;
    FrameLayout mobileLayout;

    RechargeViewModel rechargeViewModel;

    String session_id;

    EditText numberEditText;
    EditText amountEditText;
    Button rechargeButton;
    TextView providerText;
    ImageView providerImage;
    TextView selectType;
    ImageView selectTypeImage;

    ImageView contactImage;
    TextView browseTextView;

    String auth;
    String token;
    String providerName;
    String providerId;
    String mobileNumber;
    String number;
    int checkNumber;

    String serviceOperator;
    String circleId;
    String optId;
    String amount;

    private boolean isPostPaid = false;
    String layoutName;
    int PICK_CONTACT = 1337;

    private static final String TAG = RechargeActivity.class.getSimpleName();
    int PERMISSION_REQUEST_CONTACT = 1338;

    @Override
    protected void onResume() {
        super.onResume();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_from_left);

        rechargeButton.setAnimation(animation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradientbakcthree));

        auth = getResources().getString(R.string.auth_key);
        token = getResources().getString(R.string.token);

        numberEditText = findViewById(R.id.mobile_number);
        amountEditText = findViewById(R.id.amount);
        rechargeButton = findViewById(R.id.recharge_button);
        providerText = findViewById(R.id.provider_name);
        providerImage = findViewById(R.id.provider_logo);
        selectTypeImage = findViewById(R.id.select_type_image);
        ;

        contactImage = findViewById(R.id.contact_image);
        browseTextView = findViewById(R.id.browse_plans_text);
        selectType = findViewById(R.id.select_wallet);

        //Initializing ViewModel Class
        rechargeViewModel = ViewModelProviders.of(this).get(RechargeViewModel.class);

        //Initializing Mobile Layout
        mobileLayout = findViewById(R.id.mobile_recharge_layout);

        //Getting intent
        final Intent intent = getIntent();
        if (intent.hasExtra(Constants.RECHARGE)) {

            layoutName = intent.getStringExtra(Constants.MOBILE);

            //Getting the session id from intent
            session_id = intent.getStringExtra(Constants.SESSION_ID);

            if (layoutName.equals("Mobile")) {
                mobileLayout.setVisibility(View.VISIBLE);
                selectType.setVisibility(View.VISIBLE);
                selectTypeImage.setVisibility(View.VISIBLE);
                if (intent.hasExtra(Constants.PLAN_DETAILS)){

                    PlanDetails details = intent.getParcelableExtra(Constants.PLAN_DETAILS);
                    amountEditText.setText(details.getAmount());
                    SharedPreferences sharedPreferences = getSharedPreferences("Save_Details",MODE_PRIVATE);
                    mobileNumber = sharedPreferences.getString(Constants.USER_NUMBER,"");
                    numberEditText.setText(mobileNumber);
                    circleId = sharedPreferences.getString(Constants.CIRCLE_ID,"");
                    optId = sharedPreferences.getString(Constants.OPT_ID,"");
                    checkNumber = Integer.parseInt(mobileNumber.substring(0,4));
                    Log.e(TAG,"CNum is :" + checkNumber);
                    getNumberInformation(token,checkNumber);


                }
            } else if (layoutName.equals("DTH")) {
                mobileLayout.setVisibility(View.VISIBLE);
                selectTypeImage.setVisibility(View.GONE);
                selectType.setVisibility(View.GONE);
            }
            if (intent.hasExtra(Constants.ITEM_POSITION)) {
                Items items = intent.getParcelableExtra(Constants.ITEM_POSITION);
                name = items.getName();
                setTitle(name + " " + "Recharge");
            }

        }

        //Open alert dialog on select edit
        selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                browseTextView.setVisibility(View.VISIBLE);
                selectType.setText("Prepaid");
                final String[] typeList = {"Prepaid", "Postpaid"};
                final int position = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
                builder.setTitle("Choose Type");
                builder.setSingleChoiceItems(typeList, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ((which == position)) {
                            isPostPaid = false;
                            selectType.setText(typeList[0]);
                            dialog.dismiss();
                        } else {
                            isPostPaid = true;
                            selectType.setText(typeList[1]);
                            dialog.dismiss();
                        }
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        numberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (layoutName.equals("Mobile") && s.length() > 10) {

                    number = numberEditText.getText().toString().trim();
                    mobileNumber = number.substring(3);
                    numberEditText.setText(mobileNumber);
                    checkNumber = Integer.parseInt(mobileNumber.substring(0, 4));
                    getNumberInformation(token, checkNumber);
                } else if (layoutName.equals("Mobile") && s.length() == 10) {

                    mobileNumber = s.toString();
                    checkNumber = Integer.parseInt(s.toString().substring(0, 4));
                    getNumberInformation(token, checkNumber);
                } else if (layoutName.equals("Mobile") && s.length() <4) {

                    providerText.setText(null);
                    providerText.setHint(getResources().getString(R.string.select_operator));
                    Picasso.get().load(R.mipmap.formax_icon).into(providerImage);
                }else {
                    mobileNumber = s.toString().trim();
                }

            }
        });

        //On click browse plans text view
        browseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleId != null && optId != null && mobileNumber != null) {

                    SharedPreferences.Editor editor = getSharedPreferences("Save_Details",MODE_PRIVATE).edit();
                    editor.putString(Constants.USER_NUMBER, mobileNumber);
                    editor.putInt(Constants.CHECK_NUMBER,checkNumber);
                    editor.putString(Constants.CIRCLE_ID,circleId);
                    editor.putString(Constants.OPT_ID,optId);
                    editor.apply();

                    Intent planIntent = new Intent(RechargeActivity.this, PlansActivity.class);
                    planIntent.putExtra(Constants.CIRCLE_ID, circleId);
                    planIntent.putExtra(Constants.OPT_ID,optId);
                    planIntent.putExtra(Constants.SESSION_ID,session_id);
                    startActivity(planIntent);
                    finish();
                }else if (circleId == null){
                    Toast.makeText(getApplicationContext(),"Circle id missing",Toast.LENGTH_SHORT).show();
                }else if (optId == null){
                    Toast.makeText(getApplicationContext(),"opt id missing",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"number",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Process recharge request on Recharge button click
        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(rechargeButton.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                final String amount = amountEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(mobileNumber) && !TextUtils.isEmpty(amount)) {

                    LayoutInflater inflater = getLayoutInflater();
                    final View confirmLayout = inflater.inflate(R.layout.confirmation_layout, null);

                    final TextView numTextView = confirmLayout.findViewById(R.id.confirm_number);
                    final TextView amountTextView = confirmLayout.findViewById(R.id.confirm_amount);
                    final TextView retailer = confirmLayout.findViewById(R.id.confirm_retailer);
                    final FancyButton cancelButton = confirmLayout.findViewById(R.id.cancel_confirm);
                    final FancyButton confirmButton = confirmLayout.findViewById(R.id.cancel_button);
                    //Getting number and amount from edit text

                    AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this, R.style.CustomDialog);
                    builder.setView(confirmLayout);
                    builder.setCancelable(false);
                    final AlertDialog dialog = builder.create();
                    dialog.show();


                    numTextView.setText(mobileNumber.trim());
                    amountTextView.setText(amount.trim());
                    retailer.setText(providerName);
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e(TAG, mobileNumber);
                            Log.e(TAG, amount);
                            doRecharge(mobileNumber, amount);
                            dialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(RechargeActivity.this, RechargeActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            intent1.putExtra(Constants.RECHARGE, "recharge");
                            intent1.putExtra(Constants.MOBILE, layoutName);
                            intent1.putExtra(Constants.SESSION_ID, session_id);
                            startActivity(intent1);
                            finish();
                        }
                    });
                } else if (TextUtils.isEmpty(number)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Please Enter Number", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                } else if (providerName == null) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Select an Operator", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                } else if (TextUtils.isEmpty(amount)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Please Enter Amount", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Please Enter Number", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                }
            }
        });

        //On click select operator text view
        providerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutName.equals("Mobile")) {
                    if (isPostPaid) {
                        showPostPidBottomSheet();
                    } else {
                        showBottomSheetFragment();
                    }
                } else {
                    showDthBottomSheet();
                }
            }
        });

    }


    //Get information about the number user entered
    private void getNumberInformation(String token, int number) {

        rechargeViewModel.getNumberDetails(token, number).observe(this, new Observer<NumberDetect>() {
            @Override
            public void onChanged(NumberDetect numberDetect) {

                if (numberDetect != null) {

                    Log.e(TAG, "Number is detected");
                    serviceOperator = numberDetect.getService();
                    circleId = numberDetect.getCircleId();
                    optId = numberDetect.getOpId();

                    switch (optId) {
                        case "1":
                            providerId = "1";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.airtel).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "31":
                            providerId = "15";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.airtel).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "3":
                            providerId = "5";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.idea).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "33":
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.idea).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "4":
                            providerId = "3";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.bsnl).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "5":
                            providerId = "4";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.bsnl).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "32":
                            providerId = "16";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.bsnl).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "7":
                            providerId = "6";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.tatadocomo).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "8":
                            providerId = "7";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.tatadocomo).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "10":
                            providerId = "2";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.vodafone).placeholder(R.mipmap.formax_icon).into(providerImage);
                        case "30":
                            providerId = "18";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.vodafone).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "34":
                            providerId = "19";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.jio).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        case "93":
                            providerId = "8";
                            providerText.setText(serviceOperator);
                            Picasso.get().load(R.drawable.jio).placeholder(R.mipmap.formax_icon).into(providerImage);
                            break;
                        default:
                            break;
                    }
                } else {
                    Log.e(TAG, "Number not detected");
                }
            }
        });
    }

    //Make recharge status dialog method
    private void makeRechargeStatusDialog(Recharge recharge) {

        View statusLayout = getLayoutInflater().inflate(R.layout.recharge_status, null);

        ImageView imageView = statusLayout.findViewById(R.id.status_image);
        TextView textView = statusLayout.findViewById(R.id.process_text);

        TextView num = statusLayout.findViewById(R.id.recharge_number);
        TextView amount = statusLayout.findViewById(R.id.recharge_amount);

        TextView date = statusLayout.findViewById(R.id.date_time_recharge);
        TextView status = statusLayout.findViewById(R.id.recharge_status);
        TextView txnId = statusLayout.findViewById(R.id.txn_id);
        TextView optTxnId = statusLayout.findViewById(R.id.opt_txn_id);
        FancyButton button = statusLayout.findViewById(R.id.close_status);
        ImageView logoImage = statusLayout.findViewById(R.id.recharge_confirm_logo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(statusLayout);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Picasso.get().load(recharge.getLogo()).placeholder(R.mipmap.formax_round_icon).error(R.mipmap.formax_icon).into(logoImage);
        textView.setText(recharge.getMessage());
        num.setText(":    " + recharge.getNumber());
        amount.setText(":    " + recharge.getAmount());
        date.setText(":    " + recharge.getCreated_on());
        status.setText(":    " + recharge.getStatus());
        txnId.setText(":    " + recharge.getTxn_id());
        String opt = recharge.getOpt_txn_id();
        if (opt.isEmpty()) {
            optTxnId.setText(":    " + "N/A");
        } else {
            optTxnId.setText(":    " + opt);
        }
        String stat = recharge.getStatus();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeActivity.this, RechargeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra(Constants.RECHARGE, "recharge");
                intent.putExtra(Constants.MOBILE, layoutName);
                intent.putExtra(Constants.SESSION_ID, session_id);
                startActivity(intent);
                finish();
            }
        });

        if (stat.equals("PENDING")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_processing_24dp));
            textView.setTextColor(getResources().getColor(R.color.processing));
            status.setTextColor(getResources().getColor(R.color.processing));
        } else if (stat.equals("SUCCESS")) {
            textView.setTextColor(Color.GREEN);
            status.setTextColor(Color.GREEN);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_recharge_done));
        } else {
            textView.setTextColor(Color.RED);
            status.setTextColor(Color.RED);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_recharge_failed));
        }
    }

    //Method to mobile recharge on Recharge button clicked
    private void doRecharge(String mNum, String rAmount) {

        //If number and amount is not null
        View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        if (mNum != null && rAmount != null && providerId != null) {
            rechargeButton.setVisibility(View.GONE);

            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Processing", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarGreen));
            snackbar.show();

            rechargeViewModel.getRechargeStatus(session_id, auth, mNum, providerId, rAmount).observe(RechargeActivity.this, new Observer<Recharge>() {
                @Override
                public void onChanged(Recharge recharge) {
                    dialog.dismiss();

                    if (recharge != null) {
                        Log.e(TAG, "Recharge is not null");

                        if (recharge.getAmount() == null) {
                            Toast.makeText(getApplicationContext(), recharge.getNumber(), Toast.LENGTH_LONG).show();
                            providerImage.setVisibility(View.INVISIBLE);
                            rechargeButton.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(RechargeActivity.this, RechargeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            intent.putExtra(Constants.RECHARGE, "recharge");
                            intent.putExtra(Constants.MOBILE, layoutName);
                            intent.putExtra(Constants.SESSION_ID, session_id);
                            startActivity(intent);
                            finish();
                        } else {
                            providerImage.setVisibility(View.INVISIBLE);
                            rechargeButton.setVisibility(View.VISIBLE);
                            makeRechargeStatusDialog(recharge);
                        }
                    } else {
                        Log.e(TAG, "Recharge is null");
                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),mNum + ", amount: " + rAmount + ", provider: " + providerId,Toast.LENGTH_SHORT).show();
        }
    }

    public void showBottomSheetFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public void showPostPidBottomSheet() {
        PostPaidSheet postPaidSheet = new PostPaidSheet();
        postPaidSheet.show(getSupportFragmentManager(), postPaidSheet.getTag());
    }

    public void showDthBottomSheet() {
        DTHSheetFragment dthSheetFragment = new DTHSheetFragment();
        dthSheetFragment.show(getSupportFragmentManager(), dthSheetFragment.getTag());
    }

    @Override
    public void onPrepaidClick(Prepaid prepaid) {

        providerId = prepaid.getId();
        providerText.setText(prepaid.getOperatorName());
        setProviderName(prepaid.getOperatorName());
        Picasso.get().load(prepaid.getImage()).placeholder(R.mipmap.formax_round_icon).into(providerImage);

    }

    @Override
    public void onPostPaidClick(Prepaid prepaid) {

        providerId = prepaid.getId();
        Picasso.get().load(prepaid.getImage()).placeholder(R.mipmap.formax_round_icon).into(providerImage);
        setProviderName(prepaid.getOperatorName());
        providerText.setText(prepaid.getOperatorName());
    }


    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public void OnDthItemClick(Prepaid prepaid) {

        providerId = prepaid.getId();
        providerText.setText(prepaid.getOperatorName());
        setProviderName(prepaid.getOperatorName());
        Picasso.get().load(prepaid.getImage()).placeholder(R.mipmap.formax_round_icon).into(providerImage);
    }

    public void onContactImageClick(View view) {
        askForContactPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                        numberEditText.setText(number);
                    }
                    phones.close();
                    //Do something with number
                } else {
                    Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Need Contact Permission to read Contacts");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    private void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }
}

