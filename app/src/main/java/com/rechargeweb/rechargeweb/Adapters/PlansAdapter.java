package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Plans;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.PlanDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlandViewHolder> {

    private static final String TAG = PlansAdapter.class.getSimpleName();
    private Context context;
    private List<Plans> plansList = new ArrayList<>();
    OnPlanItemClickListener planItemClickListener;

    public PlansAdapter(Context context, OnPlanItemClickListener planItemClickListener) {
        this.context = context;
        this.planItemClickListener = planItemClickListener;
    }

    //Interface to add click behaviour
    public interface OnPlanItemClickListener {
        void onPlanItemClick(int position);
    }

    @NonNull
    @Override
    public PlandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PlanDetailsBinding planDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.plan_details, parent, false);
        return new PlandViewHolder(planDetailsBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PlandViewHolder holder, int position) {

        if (plansList != null) {

            Plans plans = plansList.get(position);
            holder.planDetailsBinding.setPlans(plans);
        }
    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    //Set plan list
    public void setPlansList(List<Plans> plansList) {
        this.plansList = plansList;
        notifyDataSetChanged();
    }

    //Get Plan
    public Plans getPlans(int posoition) {
        return plansList.get(posoition);
    }

    public class PlandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PlanDetailsBinding planDetailsBinding;

        public PlandViewHolder(@NonNull View itemView) {
            super(itemView);

            planDetailsBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            planItemClickListener.onPlanItemClick(position);
            Log.e(TAG,"Items Position: " + position);
        }
    }
}
