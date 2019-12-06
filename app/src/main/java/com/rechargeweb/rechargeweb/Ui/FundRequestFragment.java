package com.rechargeweb.rechargeweb.Ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rechargeweb.rechargeweb.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FundRequestFragment extends Fragment {


    public FundRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fund_request, container, false);
    }

}
