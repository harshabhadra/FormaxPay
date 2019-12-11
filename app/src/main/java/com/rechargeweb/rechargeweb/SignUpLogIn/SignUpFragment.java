package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.FragmentSignUpBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSignUpBinding signUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up,container,false);
        View view = signUpBinding.getRoot();

        return view;
    }

}
