package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.Roffer;
import com.rechargeweb.rechargeweb.databinding.SpecialOfferItemBinding;

import java.util.ArrayList;
import java.util.List;

public class RofferAdapter extends RecyclerView.Adapter<RofferAdapter.RofferViewHolder> {

    private Context context;
    private List<Roffer> rofferList = new ArrayList<>();
    private OnRofferItmeclickListener rofferItmeclickListener;

    public RofferAdapter(Context context, OnRofferItmeclickListener rofferItmeclickListener) {
        this.context = context;
        this.rofferItmeclickListener = rofferItmeclickListener;
    }

    public interface OnRofferItmeclickListener {

        void onOfferItemClick(int position);
    }

    @NonNull
    @Override
    public RofferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SpecialOfferItemBinding rofferListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.special_offer_item, parent, false);
        return new RofferViewHolder(rofferListItemBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RofferViewHolder holder, int position) {

        if (rofferList != null) {
            Roffer roffer = rofferList.get(position);
            holder.rofferListItemBinding.setSpecial(roffer);
        }
    }

    //Set offer list
    public void setRofferList(List<Roffer> rofferList) {
        this.rofferList = rofferList;
        notifyDataSetChanged();
    }

    //Get roffer item
    public Roffer getOfferItem(int position) {

        return rofferList.get(position);
    }

    @Override
    public int getItemCount() {
        return rofferList.size();
    }

    public class RofferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SpecialOfferItemBinding rofferListItemBinding;

        public RofferViewHolder(@NonNull View itemView) {
            super(itemView);

            rofferListItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            rofferItmeclickListener.onOfferItemClick(position);
        }
    }
}
