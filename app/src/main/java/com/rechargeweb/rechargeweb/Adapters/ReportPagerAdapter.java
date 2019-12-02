package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReportPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment>fragmentList = new ArrayList<>();
    private List<String>titleList = new ArrayList<>();

    public ReportPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  titleList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return ReportPagerAdapter.POSITION_NONE;
    }

    public void addFraqment(Fragment fragment, String title, int position) {

        fragmentList.add(position, fragment);
        titleList.add(position, title);
    }

    public void removeFragment(Fragment fragment, int position) {

        fragmentList.remove(position);
        titleList.remove(position);
    }
}
