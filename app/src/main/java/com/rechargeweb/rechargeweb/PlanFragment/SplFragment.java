package com.rechargeweb.rechargeweb.PlanFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rechargeweb.rechargeweb.Activity.PlansActivity;
import com.rechargeweb.rechargeweb.Adapters.PlansAdapter;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.Model.PlanDetails;
import com.rechargeweb.rechargeweb.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplFragment extends Fragment implements PlansAdapter.OnPlanItemClickListener {

    private RecyclerView planRecycler;
    private TextView planTextView;
    private PlansAdapter plansAdapter;
    private ProgressBar progressBar;

    private MainViewModel mainViewModel;

    String circleId, optId;

    String token;

    private OnSplItemClickListener splItemClickListener;

    private static final String TAG = SplFragment.class.getSimpleName();

    public SplFragment() {
        // Required empty public constructor
    }

    public interface OnSplItemClickListener{
        void onSplItemClick(PlanDetails details);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        token = getResources().getString(R.string.token);

        planRecycler = view.findViewById(R.id.plan_recycler);
        planTextView = view.findViewById(R.id.plan_text_view);
        progressBar = view.findViewById(R.id.browse_plan_loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        plansAdapter = new PlansAdapter(getContext(),SplFragment.this);
        planRecycler.setLayoutManager(linearLayoutManager);
        planRecycler.setHasFixedSize(true);
        planRecycler.setAdapter(plansAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(planRecycler.getContext(),linearLayoutManager.getOrientation());
        planRecycler.addItemDecoration(dividerItemDecoration);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        PlansActivity plansActivity = (PlansActivity)getActivity();
        if (plansActivity != null) {
            circleId = plansActivity.getCircleId();
            optId = plansActivity.getOptId();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        mainViewModel.getRechargePlans(token,"SPL",circleId,optId).observe(this, new Observer<List<PlanDetails>>() {
            @Override
            public void onChanged(List<PlanDetails> planDetails) {

                progressBar.setVisibility(View.GONE);
                if (planDetails != null){
                        PlanDetails details = planDetails.get(0);
                        if (details.getTalkTime() == null || details.getTalkTime().isEmpty()){
                            Log.e(TAG,"Error in Plan details list");
                            planTextView.setText(details.getAmount());
                            planTextView.setVisibility(View.VISIBLE);
                            planRecycler.setVisibility(View.GONE);
                        }else {
                            Log.e(TAG, "Plan details list is full");
                            planTextView.setVisibility(View.GONE);
                            planRecycler.setVisibility(View.VISIBLE);
                            plansAdapter.setPlanDetailsList(planDetails);
                        }
                }else {
                    Log.e(TAG,"Plan details list is empty");
                    planTextView.setVisibility(View.VISIBLE);
                    planTextView.setText("No Plans Available");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        splItemClickListener = (OnSplItemClickListener)getActivity();
    }

    @Override
    public void onPlanItemClick(int position) {

        PlanDetails details = plansAdapter.getPlanDetaiItem(position);
        splItemClickListener.onSplItemClick(details);
    }
}
