package com.rechargeweb.rechargeweb.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BankingAdapter extends RecyclerView.Adapter<BankingAdapter.BankingViewHolder> {

    private LayoutInflater inflater;
    private List<Items>bankingItems;
    private OnBankingItemClickListener bankingItemClickListener;

    public BankingAdapter(Context context,List<Items> bankingItems,OnBankingItemClickListener bankingItemClickListener) {

        inflater = LayoutInflater.from(context);
        this.bankingItems = bankingItems;
        this.bankingItemClickListener = bankingItemClickListener;
    }

    public interface OnBankingItemClickListener{
        public void onBankItemclick(int position);
    }

    @NonNull
    @Override
    public BankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BankingViewHolder(inflater.inflate(R.layout.pick_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankingViewHolder holder, int position) {

        if (bankingItems != null){
            holder.textView.setText(bankingItems.get(position).getName());
            Picasso.get().load(bankingItems.get(position).getImageUrl()).into(holder.imageView);
        }
    }

    //Get bank item
    public Items getBankingItem(int position){
        return bankingItems.get(position);
    }

    @Override
    public int getItemCount() {
        return bankingItems.size();
    }

    class BankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public BankingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            bankingItemClickListener.onBankItemclick(position);
        }
    }
}
