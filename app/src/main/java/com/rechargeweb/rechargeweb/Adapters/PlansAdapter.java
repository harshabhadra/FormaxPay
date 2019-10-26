package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.PlanDetails;
import com.rechargeweb.rechargeweb.R;

import java.util.ArrayList;
import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlanViewholder>{

    private LayoutInflater inflater;
    private List<PlanDetails>planDetailsList = new ArrayList<>();
    private OnPlanItemClickListener planItemClickListener;

    public PlansAdapter(Context context, OnPlanItemClickListener planItemClickListener) {

        inflater = LayoutInflater.from(context);
        this.planItemClickListener = planItemClickListener;
    }

    public interface OnPlanItemClickListener{
        void onPlanItemClick(int position);
    }

    @NonNull
    @Override
    public PlanViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanViewholder(inflater.inflate(R.layout.plan_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewholder holder, int position) {

        if (planDetailsList!= null){

            holder.amountTv.setText(planDetailsList.get(position).getAmount());

            String validity = "Validity: " + planDetailsList.get(position).getValidity();
            String talktime = "Talktime: " + planDetailsList.get(position).getTalkTime();

            holder.validityTv.setText(validity);
            holder.talktimeTv.setText(talktime);

            holder.detailsTv.setText(planDetailsList.get(position).getDetais());
        }

    }

    @Override
    public int getItemCount() {
        return planDetailsList.size();
    }

    public void setPlanDetailsList(List<PlanDetails>planDetailsList){

        this.planDetailsList = planDetailsList;
        notifyDataSetChanged();
    }

    //Get PlanDetails item
    public PlanDetails getPlanDetaiItem(int position){
        return planDetailsList.get(position);
    }

    public class PlanViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView amountTv;
        TextView validityTv;
        TextView talktimeTv;
        TextView detailsTv;

        Button selectButton;

        public PlanViewholder(@NonNull View itemView) {
            super(itemView);

            amountTv = itemView.findViewById(R.id.plan_amount);
            validityTv = itemView.findViewById(R.id.plan_validity);
            talktimeTv = itemView.findViewById(R.id.plan_talktime);
            detailsTv = itemView.findViewById(R.id.plan_details);
            selectButton = itemView.findViewById(R.id.plan_select_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            planItemClickListener.onPlanItemClick(position);
        }
    }
}
