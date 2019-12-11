package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPassSheetFragment extends BottomSheetDialogFragment {


    public ForgetPassSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.layout_custom_dialog,container,false);

     return view;
    }

}
