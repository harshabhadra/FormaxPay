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
import android.widget.TextView;
import android.widget.Toast;

import com.rechargeweb.rechargeweb.Activity.PlansActivity;
import com.rechargeweb.rechargeweb.Adapters.PlansAdapter;
import com.rechargeweb.rechargeweb.Adapters.RofferAdapter;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.Roffer;
import com.rechargeweb.rechargeweb.ViewModels.PlanViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialOfferFragment extends Fragment implements RofferAdapter.OnRofferItmeclickListener{

    private static final String TAG = TwoGFragment.class.getSimpleName();
    private RecyclerView planRecyclerView;
    private RofferAdapter rofferAdapter;
    private PlanViewModel planViewModel;

    private AlertDialog alertDialog;

    private String operatorCode;
    private String mobileNumber;
    private OnSpecialOfferItemClickListener specialOfferItemClickListener;

    public SpecialOfferFragment() {
        // Required empty public constructor
    }

    public interface OnSpecialOfferItemClickListener{
        void onSpecialOfferItemClikc(Roffer roffer);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PlansActivity plansActivity = (PlansActivity)(getActivity());
        if (plansActivity != null){
            mobileNumber = plansActivity.getMobileNumber();
            operatorCode = plansActivity.getOperatorCode();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        rofferAdapter = new RofferAdapter(getContext(),SpecialOfferFragment.this);
        planRecyclerView.setHasFixedSize(true);

        //Adding layout manager to the recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        planRecyclerView.setLayoutManager(linearLayoutManager);

        //Attach recyclerView with adapter
        planRecyclerView.setAdapter(rofferAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        planRecyclerView.addItemDecoration(dividerItemDecoration);
        getRechargeOffer(mobileNumber,operatorCode);
    }

    //Get special offer list
    private void getRechargeOffer(String number, String operator){

        alertDialog = createLoadingDialog(getContext());
        alertDialog.show();
        planViewModel.specialOfferList(number,operator).observe(getViewLifecycleOwner(), new Observer<List<Roffer>>() {
            @Override
            public void onChanged(List<Roffer> roffers) {

                alertDialog.dismiss();
                if (roffers != null){

                    Log.e(TAG,"List is not null");
                    Roffer roffer = roffers.get(0);

                    if (roffer.getDesc() == null){
                        Toast.makeText(getContext(),roffer.getRs(),Toast.LENGTH_SHORT).show();
                    }else {
                        rofferAdapter.setRofferList(roffers);
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

        specialOfferItemClickListener = (OnSpecialOfferItemClickListener)context;
    }

    @Override
    public void onOfferItemClick(int position) {

        Roffer roffer = rofferAdapter.getOfferItem(position);
        specialOfferItemClickListener.onSpecialOfferItemClikc(roffer);
    }
}
