package com.rechargeweb.rechargeweb.ReportsFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rechargeweb.rechargeweb.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebitReportFragment extends Fragment {


    public DebitReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_report, container, false);

        return view;
    }

}
