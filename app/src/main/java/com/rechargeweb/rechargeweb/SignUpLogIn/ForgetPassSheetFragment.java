package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.databinding.LayoutCustomDialogBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPassSheetFragment extends BottomSheetDialogFragment {


    private static final String TAG = ForgetPassSheetFragment.class.getSimpleName();
    private MainViewModel mainViewModel;
    private String userId;

    public ForgetPassSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutCustomDialogBinding customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_custom_dialog, container, false);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Add Text Change Listener to enter userId edit text
        customDialogBinding.forgetPassEnterUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>6){
                    customDialogBinding.forgetPassResetPassword.setEnabled(true);
                }else {
                    customDialogBinding.forgetPassResetPassword.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Set on click listener to submit button
        customDialogBinding.forgetPassResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = customDialogBinding.forgetPassEnterUserId.getText().toString().toUpperCase().trim();
                customDialogBinding.alertLoading.setVisibility(View.VISIBLE);
                customDialogBinding.forgetPassEnterUserId.setVisibility(View.INVISIBLE);
                customDialogBinding.forgetPassCancelRequest.setVisibility(View.INVISIBLE);
                customDialogBinding.forgetPassResetPassword.setVisibility(View.INVISIBLE);
                customDialogBinding.forgetPassEnterUserId.setEnabled(false);
                mainViewModel.getPasswordLiveData(userId).observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("Success")){
                            Toast.makeText(getContext(),"New Password has been sent to your registered Mobile Number",Toast.LENGTH_LONG).show();
                            dismiss();
                        }else {
                            Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                            dismiss();
                        }
                    }
                });
            }
        });

        //Set onClickListener to cancel button
        customDialogBinding.forgetPassCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return customDialogBinding.getRoot();
    }

}
