package com.rechargeweb.rechargeweb.Ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Adapters.AllReportAdapter;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.Gist.Utility;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment implements AllReportAdapter.OnReportItemClickListener {

    RecyclerView reportRecycler;
    AllReportAdapter reportAdapter;

    DummyData dummyData = new DummyData();
    String id;
    private static final String TAG = ReportFragment.class.getSimpleName();
    OnReportclickListener reportclickListener;

    public interface OnReportclickListener {
        void onReportClick(Items items);
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        reportRecycler = view.findViewById(R.id.all_report_list_recycler);
        reportRecycler.setHasFixedSize(true);
        int noOfc = Utility.calculateNoOfColumns(getContext(), 140);
        reportRecycler.setLayoutManager(new GridLayoutManager(getContext(),noOfc));
        reportAdapter = new AllReportAdapter(getContext(), ReportFragment.this, dummyData.getReportList());
        reportRecycler.setAdapter(reportAdapter);
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            id = activity.getSession_id();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
        reportRecycler.startAnimation(animation);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        reportclickListener = (OnReportclickListener) context;
    }

    @Override
    public void onReportItemClick(int position) {

        Items items = reportAdapter.getReportItem(position);
        reportclickListener.onReportClick(items);
    }
}
