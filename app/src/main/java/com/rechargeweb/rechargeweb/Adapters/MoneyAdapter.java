package com.rechargeweb.rechargeweb.Adapters;

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

import java.util.List;

public class MoneyAdapter  extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHolder> {

    LayoutInflater inflater;
    List<Items>itemsList;
    OnMoneyItemClickListener clickListener;

    public interface OnMoneyItemClickListener{

        void onMOneyItemClick(int position);
    }

    public MoneyAdapter(Context context,OnMoneyItemClickListener clickListener, List<Items> itemsList) {

        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoneyViewHolder(inflater.inflate(R.layout.money_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {

        if (itemsList != null) {
            holder.textView.setText(itemsList.get(position).getName());
            Picasso.get().load(itemsList.get(position).getImageUrl()).placeholder(R.mipmap.formax_icon).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public Items getMoneyItem(int position){
        return itemsList.get(position);
    }

    public class MoneyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public MoneyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.money_image);
            textView = itemView.findViewById(R.id.money_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onMOneyItemClick(position);
        }
    }
}
