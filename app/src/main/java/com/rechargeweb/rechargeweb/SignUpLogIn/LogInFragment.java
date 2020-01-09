package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.databinding.FragmentLoginlayoutBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {

    private static final String TAG = LogInFragment.class.getSimpleName();
    private ApiService apiService;
    private String userId, userPass;
    private AlertDialog logInDialog;
    private FragmentLoginlayoutBinding fragmentLoginlayoutBinding;
    private MainViewModel mainViewModel;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing DataBinding
        fragmentLoginlayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_loginlayout, container, false);
        View view = fragmentLoginlayoutBinding.getRoot();

        //Initializing ApiServices
        apiService = ApiUtills.getApiService();

        //Set TextChange Listener to userId input text
        fragmentLoginlayoutBinding.logInUserIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fragmentLoginlayoutBinding.logInUserIdLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentLoginlayoutBinding.logInUserIdLayout.setErrorEnabled(true);
            }
        });

        //Add textChange listener to password input text
        fragmentLoginlayoutBinding.logInPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fragmentLoginlayoutBinding.logInPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentLoginlayoutBinding.logInPasswordLayout.setErrorEnabled(true);
            }
        });

        //On LogIn Button Clicked
        fragmentLoginlayoutBinding.logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(fragmentLoginlayoutBinding.logInButton.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                userId = fragmentLoginlayoutBinding.logInUserIdInput.getText().toString().trim().toUpperCase();
                userPass = fragmentLoginlayoutBinding.logInPasswordInput.getText().toString().trim();
                if (userId.isEmpty()) {
                    fragmentLoginlayoutBinding.logInUserIdLayout.setError("Enter User ID");
                } else if (userPass.isEmpty()) {
                    fragmentLoginlayoutBinding.logInPasswordInput.setError("Enter Password");
                } else {

                    //Show loading dialog and logIn user
                    logInDialog = createAlertDialog(getContext());
                    logInDialog.show();
                    logInUser(userId, userPass);
                }
            }
        });

        //On Forget Password Button click
        fragmentLoginlayoutBinding.forgetPassTvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassSheetFragment passSheetFragment = new ForgetPassSheetFragment();
                passSheetFragment.setCancelable(false);
                passSheetFragment.show(getFragmentManager(),passSheetFragment.getTag());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Getting values from shared preference and if not empty then log in user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SAVE_ID_PASS, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(Constants.USER_ID, null);
        String pass = sharedPreferences.getString(Constants.PASSWORD, null);
        if (user != null && pass != null) {

            fragmentLoginlayoutBinding.logInUserIdInput.setText(user);
            fragmentLoginlayoutBinding.logInPasswordInput.setText(pass);
            logInDialog = createAlertDialog(getContext());
            logInDialog.show();
            logInUser(user, pass);
        } else {
            Log.e(TAG, "empty information");
        }
    }

    //Log In User
    private void logInUser(String id, String password) {

        apiService.savePost(id, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Post post) {

                        logInDialog.dismiss();
                        Log.e(TAG, "Log in user on next: " + post.getLogin_id());

                        String session_id = post.getSession_id();
                        Log.e(TAG, "Session Id is " + session_id);

                        if (post.getMessage().equals(getResources().getString(R.string.retailer_access))) {

                            //Save User Id and password in Shared Preference
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SAVE_ID_PASS, Context.MODE_PRIVATE).edit();
                            editor.putString(Constants.USER_ID, id);
                            editor.putString(Constants.PASSWORD, password);
                            editor.apply();

                            //Send User to HomeActivity
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra(Constants.SESSION_ID, session_id);
                            intent.putExtra(Constants.USER_ID, id);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Error: " + post.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "Error Login user");
                        logInDialog.dismiss();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //Create Loading Dialog
    private AlertDialog createAlertDialog(Context context) {

        View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(layout);
        return builder.create();
    }
}
