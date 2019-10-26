package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rechargeweb.rechargeweb.PlanFragment.DataFragment;
import com.rechargeweb.rechargeweb.PlanFragment.FttFragment;
import com.rechargeweb.rechargeweb.PlanFragment.RmgFragment;
import com.rechargeweb.rechargeweb.PlanFragment.SplFragment;
import com.rechargeweb.rechargeweb.PlanFragment.TupFragment;

public class PlanPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String [] planArray = {"SPL, DATA, FTT, TUP, RMG"};

    public PlanPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

      if (position == 0){
          return new SplFragment();
      }else if (position == 1){
          return new DataFragment();
      }else if (position == 2){
          return new FttFragment();
      }else if (position == 3){
          return new TupFragment();
      }else {
          return new RmgFragment();
      }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0){
            return "Special";
        }else if (position == 1){
            return "Data";
        }else if (position == 2){
            return "FTT";
        }else if (position == 3){
            return "Top Up";
        }else {
            return "Roaming";
        }
    }
}
