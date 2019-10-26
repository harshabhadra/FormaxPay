package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.RechargeDetails;
import com.rechargeweb.rechargeweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    LayoutInflater inflater;
    List<RechargeDetails> detailsList = new ArrayList<>();
    OnDetailsItemClickListener clickListener;

    public interface OnDetailsItemClickListener{
        void onDetailsItemClick(int position);
    }

    public DetailsAdapter(Context context, OnDetailsItemClickListener clickListener, List<RechargeDetails> detailsList) {

        inflater = LayoutInflater.from(context);
        this.clickListener  = clickListener;
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsViewHolder(inflater.inflate(R.layout.recharge_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailsViewHolder holder, final int position) {

        final RechargeDetails rechargeDetails = detailsList.get(position);
        holder.bind(rechargeDetails);
    }

    @Override
    public int getItemCount() {
        if (detailsList != null) {
            return detailsList.size();
        } else {
            return 0;
        }
    }

    public RechargeDetails getDetailsItem(int position){
        return detailsList.get(position);
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView operatorLogo;
        TextView number;
        TextView amount;
        TextView Status;
        TextView date;
        CardView cardView;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            operatorLogo = itemView.findViewById(R.id.details_logo);
            number = itemView.findViewById(R.id.details_number);
            amount = itemView.findViewById(R.id.details_amount);
            Status = itemView.findViewById(R.id.details_status);
            date = itemView.findViewById(R.id.details_date);
            cardView = itemView.findViewById(R.id.details_card);
            itemView.setOnClickListener(this);

        }

        private void bind(RechargeDetails rechargeDetails) {

            if (!rechargeDetails.getLogo().equals("N/A")){
                Picasso.get().load(rechargeDetails.getLogo()).placeholder(R.mipmap.formax_round_icon).error(R.mipmap.formax_icon).into(operatorLogo);
            }
            number.setText(rechargeDetails.getNumber());
            amount.setText(rechargeDetails.getAmount());
            date.setText(rechargeDetails.getCreated_on());
            String status = rechargeDetails.getStatus();
            Status.setText(status);
            if (status.equals("SUCCESS")) {
                Status.setTextColor(Color.parseColor("#009624"));
//                cardView.setStrokeColor(Color.parseColor("#009624"));
//                cardView.setStrokeWidth(5);
            } else if (status.equals("FAILED")) {
                Status.setTextColor(Color.RED);
//                cardView.setStrokeColor(Color.RED);
//                cardView.setStrokeWidth(5);
            } else {
//                cardView.setStrokeWidth(5);
                Status.setTextColor(Color.parseColor("#ff9e22"));
//                cardView.setStrokeColor(Color.parseColor("#ff9e22"));
            }
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onDetailsItemClick(position);
        }
    }
}
