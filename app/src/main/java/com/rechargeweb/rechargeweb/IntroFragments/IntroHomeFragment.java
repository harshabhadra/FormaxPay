package com.rechargeweb.rechargeweb.IntroFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rechargeweb.rechargeweb.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroHomeFragment extends Fragment {


    public IntroHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_home, container, false);
    }

}
