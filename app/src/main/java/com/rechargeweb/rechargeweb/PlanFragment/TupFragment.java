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
public class TupFragment extends Fragment implements PlansAdapter.OnPlanItemClickListener {

    RecyclerView planRecycler;
    TextView planTextView;
    PlansAdapter plansAdapter;

    MainViewModel mainViewModel;

    String circleId, optId;

    String token;

    private OnTopUpItemClickListener topUpItemClickListener;
    private static final String TAG = TupFragment.class.getSimpleName();
    public TupFragment() {
        // Required empty public constructor
    }

    public interface OnTopUpItemClickListener{

        void onTopUpItemCick(PlanDetails details);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        token = getResources().getString(R.string.token);

        planRecycler = view.findViewById(R.id.plan_recycler);
        planTextView = view.findViewById(R.id.plan_text_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        plansAdapter = new PlansAdapter(getContext(),TupFragment.this);
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
        mainViewModel.getRechargePlans(token,"TUP",circleId,optId).observe(this, new Observer<List<PlanDetails>>() {
            @Override
            public void onChanged(List<PlanDetails> planDetails) {

                if (planDetails != null){

                    PlanDetails details = planDetails.get(0);
                    if (details.getTalkTime() == null || details.getTalkTime().isEmpty()){
                        Log.e(TAG,"Error in loading plans");
                        planTextView.setText(details.getAmount());
                        planTextView.setVisibility(View.VISIBLE);
                        planRecycler.setVisibility(View.GONE);
                    }else {
                        Log.e(TAG, "Plan details list is full");
                        plansAdapter.setPlanDetailsList(planDetails);
                        planTextView.setVisibility(View.GONE);
                        planRecycler.setVisibility(View.VISIBLE);
                    }
                }else {
                    Log.e(TAG,"Plan details list is empty");
                    planRecycler.setVisibility(View.GONE);
                    planTextView.setVisibility(View.VISIBLE);
                    planTextView.setText("No Plans Available");
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        topUpItemClickListener = (OnTopUpItemClickListener)getActivity();
    }

    @Override
    public void onPlanItemClick(int position) {

        PlanDetails details = plansAdapter.getPlanDetaiItem(position);
        topUpItemClickListener.onTopUpItemCick(details);
    }
}
