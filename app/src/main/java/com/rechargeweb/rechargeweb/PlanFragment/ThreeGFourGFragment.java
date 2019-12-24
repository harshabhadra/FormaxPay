package com.rechargeweb.rechargeweb.PlanFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.rechargeweb.rechargeweb.Activity.PlansActivity;
import com.rechargeweb.rechargeweb.Plans;
import com.rechargeweb.rechargeweb.Adapters.PlansAdapter;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.PlanViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeGFourGFragment extends Fragment implements PlansAdapter.OnPlanItemClickListener {

    private static final String TAG = ThreeGFourGFragment.class.getSimpleName();
    private RecyclerView planRecyclerView;
    private PlansAdapter plansAdapter;
    private PlanViewModel planViewModel;

    private AlertDialog alertDialog;

    private String operatorCode;
    private String circleCode;
    private OnThreeGForuGItemClickListener threeGForuGItemClickListener;

    public ThreeGFourGFragment() {
        // Required empty public constructor
    }

    public interface OnThreeGForuGItemClickListener{
        void onThreeGForuGItemClick(Plans plans);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        planRecyclerView = view.findViewById(R.id.plan_recycler);

        //Initializing PlanViewModel Class
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Initializing adapter
        plansAdapter = new PlansAdapter(getContext(),ThreeGFourGFragment.this);
        planRecyclerView.setHasFixedSize(true);

        //Adding layout manager to the recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        planRecyclerView.setLayoutManager(linearLayoutManager);

        //Attach recyclerView with adapter
        planRecyclerView.setAdapter(plansAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        planRecyclerView.addItemDecoration(dividerItemDecoration);
        getRechargePlans(circleCode,operatorCode,"3G/4G");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlansActivity plansActivity = (PlansActivity)(getActivity());
        if (plansActivity != null){
            operatorCode = plansActivity.getOperatorCode();
            circleCode = plansActivity.getCircleCode();
            Log.e(TAG,"Operator Code: " + operatorCode + "circle code: " + circleCode);
        }
    }

    //Method to get recharge plans
    private void getRechargePlans(String circleId, String operator,String type) {

        alertDialog = createLoadingDialog(getContext());
        //Show alert dialog
        alertDialog.show();

        planViewModel.getMobileRechargePlanList(circleId,operator,type).observe(getViewLifecycleOwner(), new Observer<List<Plans>>() {
            @Override
            public void onChanged(List<Plans> plans) {

                alertDialog.dismiss();
                if (plans != null){

                    if (plans.get(0).getDesc()!= null) {
                        Log.e(TAG, "Plan list is not null");
                        plansAdapter.setPlansList(plans);
                    }else {
                        Toast.makeText(getContext(),plans.get(0).getRs(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.e(TAG,"List is null");
                }
            }
        });
    }

    //Create Alert Dialog
    private AlertDialog createLoadingDialog(Context context) {
        View layout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(false);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        threeGForuGItemClickListener = (OnThreeGForuGItemClickListener)context;
    }

    @Override
    public void onPlanItemClick(int position) {

        Plans plans = plansAdapter.getPlans(position);
        threeGForuGItemClickListener.onThreeGForuGItemClick(plans);
    }
}
