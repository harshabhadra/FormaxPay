package com.rechargeweb.rechargeweb.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    Button secureLogIn;
    ApiService apiService;

    String userId;
    String userPass;
    String postResponse;

    TextView forgotPassTV;
    TextView newUserTv;

    MainViewModel mainViewModel;

    private boolean isLogOut;

    LinearLayout signInLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signInLayout = findViewById(R.id.sign_in_layout);
        //Initializing Api services
        apiService = ApiUtills.getApiService();

        //Initializing ViewModel class
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Initializing edit text
        final EditText userName = findViewById(R.id.user_name);
        final EditText password = findViewById(R.id.password);


        //Initializing LogIn button
        secureLogIn = findViewById(R.id.log_In);
        newUserTv = findViewById(R.id.new_user_tv);

        final Intent intent = getIntent();

        //If a new user
        if (intent.hasExtra("logout")) {
            isLogOut = intent.getBooleanExtra("logout", true);
            SharedPreferences.Editor editor = getSharedPreferences("UserLogOut", MODE_PRIVATE).edit();
            editor.putBoolean("userLogOut", isLogOut);
            editor.apply();

            SharedPreferences intentPreferences = getSharedPreferences(Constants.SAVE_ID_PASS, MODE_PRIVATE);
            intentPreferences.edit().clear().apply();
        } else {
            SharedPreferences preferences = getSharedPreferences("UserLogOut", MODE_PRIVATE);
            isLogOut = preferences.getBoolean("userLogOut", true);
            if (!isLogOut) {
                //Getting values from SharedPreference
                signInLayout.setVisibility(View.INVISIBLE);
                AlertDialog dialog = createLoadingDialog(this);
                dialog.show();
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SAVE_ID_PASS, MODE_PRIVATE);
                userId = sharedPreferences.getString(Constants.USER_ID, "");
                userPass = sharedPreferences.getString(Constants.PASSWORD, "");
                userName.setText(userId);
                password.setText(userPass);
                sendPost(userId, userPass,dialog);
            }
        }

        //Initializing forget password Text View
        forgotPassTV = findViewById(R.id.forgot_pass);
        forgotPassTV.setPaintFlags(forgotPassTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(forgotPassTV.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                final EditText userId = alertLayout.findViewById(R.id.enter_user_id);

                final Button resetButton = alertLayout.findViewById(R.id.reset_password);
                final Button cancelButton = alertLayout.findViewById(R.id.cancel_request);
                final TextView textView = alertLayout.findViewById(R.id.password_message);
                final Button okButton = alertLayout.findViewById(R.id.cancel_button);
                final ProgressBar alertLoading = alertLayout.findViewById(R.id.alert_loading);

                //Add text changed listener to User Id edit text
                userId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() > 0) {
                            resetButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Reset Password");
                builder.setIcon(getResources().getDrawable(R.mipmap.formax_round_icon));
                builder.setView(alertLayout);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetButton.setVisibility(View.INVISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        userId.setVisibility(View.GONE);
                        alertLoading.setVisibility(View.VISIBLE);
                        String uName = userId.getText().toString().toUpperCase();
                        Log.e(TAG, "User name is: " + uName);
                        if (!TextUtils.isEmpty(uName)) {
                            mainViewModel.getPasswordLiveData(uName).observe(MainActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {

                                    alertLoading.setVisibility(View.GONE);
                                    Log.e(TAG, "response change: " + s);
                                    if (s.equals("Success")) {
                                        userId.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        okButton.setVisibility(View.VISIBLE);
                                        textView.setText("New Password has been sent to your registered Phone No.");
                                        textView.setTextColor(Color.GREEN);
                                    } else {
                                        textView.setVisibility(View.VISIBLE);
                                        resetButton.setVisibility(View.VISIBLE);
                                        textView.setText(s);
                                        textView.setTextColor(Color.RED);
                                        cancelButton.setVisibility(View.VISIBLE);
                                        userId.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        } else {
                            userId.setText("Enter User Id");
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (inputManager != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(intent1);
                        alertDialog.dismiss();
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (inputManager != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(intent1);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //Setting on Click listener to Log in button
        secureLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(secureLogIn.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                userId = userName.getText().toString().trim().toUpperCase();
                userPass = password.getText().toString().trim();
                isLogOut = false;
                SharedPreferences.Editor editor = getSharedPreferences("UserLogOut", MODE_PRIVATE).edit();
                editor.putBoolean("userLogOut", isLogOut);
                editor.apply();

                //Sending post request if user name and password is not empty
                if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPass)) {
                    secureLogIn.setVisibility(View.GONE);

                    AlertDialog dialog = createLoadingDialog(MainActivity.this);
                    dialog.show();
                    forgotPassTV.setVisibility(View.INVISIBLE);
                    newUserTv.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
                            "Signing In", Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarGreen));
                    snackbar.show();

                    //Send Network request to login
                    sendPost(userId, userPass,dialog);
                } else if (TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPass)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.enteruserid), Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                } else if (!TextUtils.isEmpty(userId) && TextUtils.isEmpty(userPass)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.enterpassword), Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                    snackbar.show();
                }
            }
        });
    }

    //Sending Post request to log in
    private void sendPost(String user, String password, final AlertDialog dialog) {

        Log.e(TAG, "sendPost");
        apiService.savePost(user, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "completed");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "error");
                        Log.e(TAG, e.getMessage());
                        dialog.dismiss();
                        secureLogIn.setVisibility(View.VISIBLE);
                        signInLayout.setVisibility(View.VISIBLE);
                        forgotPassTV.setVisibility(View.VISIBLE);
                        newUserTv.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), e.getMessage(), Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                        snackbar.show();
                    }

                    @Override
                    public void onNext(Post post) {
                        Log.e(TAG, "success: " + post.toString());
                        //Getting the post response
                        postResponse = post.getMessage();

                        final String session_id = post.getSession_id();
                        Log.e(TAG, "Session: " + session_id);

                        //If response is success then log in
                        if (postResponse.equals(getResources().getString(R.string.retailer_access))) {

                            dialog.dismiss();
                            SharedPreferences.Editor editor = getSharedPreferences(Constants.SAVE_ID_PASS, MODE_PRIVATE).edit();
                            editor.putString(Constants.USER_ID, userId);
                            editor.putString(Constants.PASSWORD, userPass);
                            editor.apply();
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.loginsuccess), Snackbar.LENGTH_SHORT);
                            View view = snackbar.getView();
                            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarGreen));
                            snackbar.show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.SESSION_ID, session_id);
                            intent.putExtra(Constants.USER_ID, userId);
                            startActivity(intent);
                            finish();

                        } else {
                            dialog.dismiss();
                            secureLogIn.setVisibility(View.VISIBLE);
                            signInLayout.setVisibility(View.VISIBLE);
                            forgotPassTV.setVisibility(View.VISIBLE);
                            newUserTv.setVisibility(View.VISIBLE);
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), postResponse, Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBarRed));
                            snackbar.show();
                        }
                    }
                });
    }

    //Create Alert Dialog
    private AlertDialog createLoadingDialog(Context context){
        View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

}
